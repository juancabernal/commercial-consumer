package com.eatup.commercial.service.sale.impl;

import com.eatup.commercial.domain.sale.SaleDomain;
import com.eatup.commercial.domain.sale.SaleStatus;
import com.eatup.commercial.exception.InvalidSaleStatusTransitionException;
import com.eatup.commercial.exception.SalePatchProcessingException;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.repository.sale.SaleRepository;
import com.eatup.commercial.service.sale.SaleService;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
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

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
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
