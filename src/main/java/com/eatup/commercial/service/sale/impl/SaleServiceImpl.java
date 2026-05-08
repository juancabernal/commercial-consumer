package com.eatup.commercial.service.sale.impl;

import com.eatup.commercial.domain.sale.*;
import com.eatup.commercial.exception.InvalidSaleStatusTransitionException;
import com.eatup.commercial.exception.SaleCreateProcessingException;
import com.eatup.commercial.exception.SaleDeleteProcessingException;
import com.eatup.commercial.exception.SalePatchProcessingException;
import com.eatup.commercial.exception.SaleUpdateProcessingException;
import com.eatup.commercial.messaging.sales.SaleCreateResponseMessage;
import com.eatup.commercial.messaging.sales.SaleDeleteResponseMessage;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.messaging.sales.SaleRecipeResponseMessage;
import com.eatup.commercial.messaging.sales.SaleUpdateResponseMessage;
import com.eatup.commercial.repository.sale.RecipePreparationTraceRepository;
import com.eatup.commercial.repository.sale.SaleRepository;
import com.eatup.commercial.service.sale.SaleService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements SaleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SaleServiceImpl.class);
    private static final Map<SaleStatus, EnumSet<SaleStatus>> ALLOWED_TRANSITIONS = new EnumMap<>(SaleStatus.class);

    static {
        ALLOWED_TRANSITIONS.put(SaleStatus.CREATED, EnumSet.of(SaleStatus.IN_PROGRESS, SaleStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(SaleStatus.IN_PROGRESS, EnumSet.of(SaleStatus.COMPLETED, SaleStatus.CANCELLED));
        ALLOWED_TRANSITIONS.put(SaleStatus.COMPLETED, EnumSet.noneOf(SaleStatus.class));
        ALLOWED_TRANSITIONS.put(SaleStatus.CANCELLED, EnumSet.noneOf(SaleStatus.class));
    }

    private final SaleRepository saleRepository;
    private final RecipePreparationTraceRepository traceRepository;

    public SaleServiceImpl(SaleRepository saleRepository, RecipePreparationTraceRepository traceRepository) {
        this.saleRepository = saleRepository;
        this.traceRepository = traceRepository;
    }

    @Override
    public void applyPatch(SalePatchRequestedMessage message) {
        if (message == null || message.getSaleId() == null || message.getRequest() == null
                || message.getRequest().getStatus() == null) {
            throw new SalePatchProcessingException(
                    "El mensaje de patch de venta está incompleto. Debe incluir saleId y request.status.");
        }

        SaleDomain sale = saleRepository.findById(message.getSaleId()).orElseThrow(() ->
                new SalePatchProcessingException(
                        "No se encontró la venta con id " + message.getSaleId() + " para aplicar el cambio de estado."));

        String requestedStatus = message.getRequest().getStatus().trim().toUpperCase();
        final SaleStatus nextStatus;
        try {
            nextStatus = SaleStatus.valueOf(requestedStatus);
        } catch (IllegalArgumentException e) {
            throw new SalePatchProcessingException(
                    "Estado de venta inválido recibido en el mensaje de patch: " + requestedStatus + ".", e);
        }

        LOGGER.info("Applying sale status patch. saleId={}, currentStatus={}, requestedStatus={}",
                sale.getId(), sale.getStatus(), nextStatus);

        validateTransition(sale.getStatus(), nextStatus);

        sale.setStatus(nextStatus);
        sale.setModifiedDate(LocalDateTime.now());
        saleRepository.save(sale);
        LOGGER.info("Sale status updated successfully. saleId={}, newStatus={}", sale.getId(), nextStatus);
    }

    @Override
    @Transactional
    public void applyCreateResponse(SaleCreateResponseMessage message) {
        try {
            validateCreateMessage(message);
            boolean allApproved = message.getRecipes().stream()
                    .allMatch(recipe -> Boolean.TRUE.equals(recipe.getApproved()));
            SaleStatus saleStatus = allApproved ? SaleStatus.CREATED : SaleStatus.CANCELLED;

            LocalDateTime now = LocalDateTime.now();
            SaleDomain sale = new SaleDomain();
            sale.setId(message.getIdMessage());
            sale.setLocationId(message.getLocationId());
            sale.setSellerId(message.getSellerId().trim());
            sale.setTableId(message.getTableId().trim());
            sale.setStatus(saleStatus);
            sale.setCreatedDate(now);
            sale.setModifiedDate(now);

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (SaleRecipeResponseMessage recipe : message.getRecipes()) {
                BigDecimal subtotal = recipe.getSubtotal() != null ? recipe.getSubtotal() : recipe.getUnitPrice().multiply(recipe.getQuantity());
                totalAmount = totalAmount.add(subtotal);

                SaleDetailDomain detail = new SaleDetailDomain();
                detail.setRecipeId(recipe.getRecipeId());
                detail.setLineDisplayName(recipe.getLineDisplayName());
                detail.setRecipeLineComment(recipe.getRecipeLineComment());
                detail.setQuantity(recipe.getQuantity());
                detail.setUnitPrice(recipe.getUnitPrice());
                detail.setSubtotal(subtotal);
                detail.setCreatedDate(now);
                detail.setModifiedDate(now);
                sale.addDetail(detail);
            }
            sale.setTotalAmount(totalAmount);

            SaleDomain savedSale = saleRepository.save(sale);

            List<RecipePreparationTraceDomain> traces = new ArrayList<>();
            for (SaleDetailDomain detail : savedSale.getDetails()) {
                SaleRecipeResponseMessage recipe = message.getRecipes().stream()
                        .filter(r -> r.getRecipeId().equals(detail.getRecipeId()))
                        .findFirst()
                        .orElseThrow(() -> new SaleCreateProcessingException("No se encontró receta para detalle guardado."));

                RecipePreparationTraceDomain trace = new RecipePreparationTraceDomain();
                trace.setSaleId(savedSale.getId());
                trace.setSaleDetailId(detail.getId());
                trace.setRecipeId(detail.getRecipeId());
                trace.setStatus(Boolean.TRUE.equals(recipe.getApproved())
                        ? RecipePreparationTraceStatus.ACCEPTED
                        : RecipePreparationTraceStatus.REJECTED);
                trace.setObservation(resolveObservation(recipe));
                trace.setCreatedDate(now);
                trace.setModifiedDate(now);
                traces.add(trace);
            }
            traceRepository.saveAll(traces);

            if (allApproved) {
                LOGGER.info("Sale create response accepted. saleId={}, recipes={}",
                        savedSale.getId(), message.getRecipes().size());
            } else {
                List<String> rejectedRecipes = message.getRecipes().stream()
                        .filter(r -> Boolean.FALSE.equals(r.getApproved()))
                        .map(r -> "{recipeId=" + r.getRecipeId() + ", message='" + resolveObservation(r) + "'}")
                        .collect(Collectors.toList());
                LOGGER.warn("Sale create response contains rejected recipes. saleId={}, sale stored as CANCELLED. Rejected recipes={}",
                        savedSale.getId(), rejectedRecipes);
            }
        } catch (Exception e) {
            LOGGER.error("Failed to apply sale create response. idMessage={}. Error: {}",
                    message != null ? message.getIdMessage() : null, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void applyUpdateResponse(SaleUpdateResponseMessage message) {
        try {
            validateUpdateMessage(message);

            boolean allApproved = message.getSale().getRecipes().stream()
                    .allMatch(recipe -> Boolean.TRUE.equals(recipe.getApproved()));

            if (!allApproved) {
                List<String> rejectedRecipesDescription = message.getSale().getRecipes().stream()
                        .filter(recipe -> Boolean.FALSE.equals(recipe.getApproved()))
                        .map(recipe -> "{recipeId=" + recipe.getRecipeId() + ", message='" + resolveObservation(recipe) + "'}")
                        .collect(Collectors.toList());
                LOGGER.warn("Sale update rejected by inventory. saleId={}, rejectedRecipes={}",
                        message.getSaleId(), rejectedRecipesDescription);
                return;
            }

            SaleDomain sale = saleRepository.findById(message.getSaleId()).orElseThrow(() ->
                    new SaleUpdateProcessingException(
                            "No se encontró la venta con id " + message.getSaleId() + " para aplicar la actualización."));

            if (sale.getStatus() == SaleStatus.COMPLETED) {
                throw new SaleUpdateProcessingException("No se puede actualizar una venta en estado COMPLETED.");
            }

            LocalDateTime now = LocalDateTime.now();
            sale.setLocationId(message.getSale().getLocationId());
            sale.setSellerId(message.getSale().getSellerId().trim());
            sale.setTableId(message.getSale().getTableId().trim());
            sale.setModifiedDate(now);

            traceRepository.deleteBySaleId(sale.getId());
            sale.getDetails().clear();

            BigDecimal totalAmount = BigDecimal.ZERO;
            for (SaleRecipeResponseMessage recipe : message.getSale().getRecipes()) {
                BigDecimal subtotal = recipe.getSubtotal() != null
                        ? recipe.getSubtotal()
                        : recipe.getUnitPrice().multiply(recipe.getQuantity());
                totalAmount = totalAmount.add(subtotal);

                SaleDetailDomain detail = new SaleDetailDomain();
                detail.setRecipeId(recipe.getRecipeId());
                detail.setLineDisplayName(recipe.getLineDisplayName());
                detail.setRecipeLineComment(recipe.getRecipeLineComment());
                detail.setQuantity(recipe.getQuantity());
                detail.setUnitPrice(recipe.getUnitPrice());
                detail.setSubtotal(subtotal);
                detail.setCreatedDate(now);
                detail.setModifiedDate(now);
                sale.addDetail(detail);
            }
            sale.setTotalAmount(totalAmount);

            SaleDomain savedSale = saleRepository.saveAndFlush(sale);

            List<RecipePreparationTraceDomain> traces = new ArrayList<>();
            for (SaleDetailDomain detail : savedSale.getDetails()) {
                SaleRecipeResponseMessage recipe = message.getSale().getRecipes().stream()
                        .filter(r -> r.getRecipeId().equals(detail.getRecipeId()))
                        .findFirst()
                        .orElseThrow(() -> new SaleUpdateProcessingException("No se encontró receta para detalle actualizado."));

                RecipePreparationTraceDomain trace = new RecipePreparationTraceDomain();
                trace.setSaleId(savedSale.getId());
                trace.setSaleDetailId(detail.getId());
                trace.setRecipeId(detail.getRecipeId());
                trace.setStatus(RecipePreparationTraceStatus.ACCEPTED);
                trace.setObservation(resolveObservation(recipe));
                trace.setCreatedDate(now);
                trace.setModifiedDate(now);
                traces.add(trace);
            }
            traceRepository.saveAll(traces);

            LOGGER.info("Sale update response accepted and applied. saleId={}, recipes={}",
                    savedSale.getId(), message.getSale().getRecipes().size());
        } catch (Exception e) {
            LOGGER.error("Failed to apply sale update response. saleId={}. Error: {}",
                    message != null ? message.getSaleId() : null, e.getMessage(), e);
            throw e;
        }
    }


    @Override
    @Transactional
    public void applyDeleteResponse(SaleDeleteResponseMessage message) {
        try {
            validateDeleteMessage(message);

            if (Boolean.FALSE.equals(message.getApproved())) {
                LOGGER.warn(
                        "Sale delete rejected by inventory. saleId={}, reason={}",
                        message.getSaleId(),
                        resolveDeleteMessage(message)
                );
                return;
            }

            Optional<SaleDomain> saleOpt = saleRepository.findById(message.getSaleId());
            if (saleOpt.isEmpty()) {
                LOGGER.warn(
                        "Sale delete response received but sale does not exist. saleId={}. Message will be ignored as idempotent delete.",
                        message.getSaleId()
                );
                return;
            }

            SaleDomain sale = saleOpt.get();
            if (sale.getStatus() == SaleStatus.COMPLETED) {
                LOGGER.warn("Cannot delete sale because it is COMPLETED. saleId={}", sale.getId());
                return;
            }

            traceRepository.deleteBySaleId(sale.getId());
            saleRepository.delete(sale);

            LOGGER.info(
                    "Sale deleted successfully after inventory approval. saleId={}, inventoryMessage={}",
                    sale.getId(),
                    resolveDeleteMessage(message)
            );
        } catch (Exception e) {
            LOGGER.error(
                    "Failed to apply sale delete response. saleId={}. Error: {}",
                    message != null ? message.getSaleId() : null,
                    e.getMessage(),
                    e
            );
            throw e;
        }
    }

    private void validateUpdateMessage(SaleUpdateResponseMessage message) {
        if (message == null || message.getSaleId() == null || message.getSale() == null) {
            throw new SaleUpdateProcessingException(
                    "El mensaje de actualización de venta está incompleto. Debe incluir saleId y sale.");
        }

        if (message.getSale().getLocationId() == null
                || message.getSale().getSellerId() == null || message.getSale().getSellerId().isBlank()
                || message.getSale().getTableId() == null || message.getSale().getTableId().isBlank()
                || message.getSale().getRecipes() == null || message.getSale().getRecipes().isEmpty()) {
            throw new SaleUpdateProcessingException(
                    "El payload de actualización de venta está incompleto. Debe incluir locationId, sellerId, tableId y recipes.");
        }

        for (SaleRecipeResponseMessage recipe : message.getSale().getRecipes()) {
            if (recipe == null || recipe.getRecipeId() == null || recipe.getQuantity() == null
                    || recipe.getUnitPrice() == null || recipe.getApproved() == null
                    || recipe.getQuantity().compareTo(BigDecimal.ZERO) <= 0
                    || recipe.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SaleUpdateProcessingException(
                        "Una receta del mensaje de actualización de venta está incompleta. Debe incluir recipeId, quantity, unitPrice y approved.");
            }
        }
    }

    private String resolveObservation(SaleRecipeResponseMessage recipe) {
        if (recipe.getMessage() != null && !recipe.getMessage().isBlank()) {
            return recipe.getMessage();
        }
        return Boolean.TRUE.equals(recipe.getApproved()) ? "Receta aprobada" : "Receta rechazada por inventory";
    }

    private void validateCreateMessage(SaleCreateResponseMessage message) {
        if (message == null || message.getIdMessage() == null || message.getLocationId() == null
                || message.getSellerId() == null || message.getSellerId().isBlank()
                || message.getTableId() == null || message.getTableId().isBlank()
                || message.getRecipes() == null || message.getRecipes().isEmpty()) {
            throw new SaleCreateProcessingException(
                    "El mensaje de creación de venta está incompleto. Debe incluir idMessage, locationId, sellerId, tableId y recipes.");
        }

        for (SaleRecipeResponseMessage recipe : message.getRecipes()) {
            if (recipe == null || recipe.getRecipeId() == null || recipe.getQuantity() == null
                    || recipe.getUnitPrice() == null || recipe.getApproved() == null
                    || recipe.getQuantity().compareTo(BigDecimal.ZERO) <= 0
                    || recipe.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new SaleCreateProcessingException(
                        "Una receta del mensaje de creación de venta está incompleta. Debe incluir recipeId, quantity, unitPrice y approved.");
            }
        }
    }


    private void validateDeleteMessage(SaleDeleteResponseMessage message) {
        if (message == null || message.getSaleId() == null || message.getApproved() == null) {
            throw new SaleDeleteProcessingException(
                    "El mensaje de eliminación de venta está incompleto. Debe incluir saleId y approved.");
        }
    }

    private String resolveDeleteMessage(SaleDeleteResponseMessage message) {
        if (message.getMessage() != null && !message.getMessage().isBlank()) {
            return message.getMessage();
        }
        return Boolean.TRUE.equals(message.getApproved())
                ? "Delete aprobado por inventory."
                : "Delete rechazado por inventory.";
    }

    private void validateTransition(SaleStatus currentStatus, SaleStatus nextStatus) {
        if (Objects.equals(currentStatus, nextStatus)) {
            return;
        }

        if (currentStatus == SaleStatus.COMPLETED) {
            throw new InvalidSaleStatusTransitionException("No se puede modificar una venta en estado COMPLETED.");
        }

        if (currentStatus == SaleStatus.CANCELLED) {
            throw new InvalidSaleStatusTransitionException("No se puede modificar una venta en estado CANCELLED.");
        }

        EnumSet<SaleStatus> allowedTargets = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(SaleStatus.class));
        if (!allowedTargets.contains(nextStatus)) {
            throw new InvalidSaleStatusTransitionException(
                    "No se puede cambiar el estado de la venta de " + currentStatus + " a " + nextStatus + ".");
        }
    }
}
