package com.eatup.commercial.service.purchase.impl;

import com.eatup.commercial.domain.purchase.PurchaseDomain;
import com.eatup.commercial.domain.purchase.PurchaseItemDomain;
import com.eatup.commercial.domain.purchase.PurchaseStatus;
import com.eatup.commercial.messaging.inventory.InventoryMessagePublisher;
import com.eatup.commercial.messaging.inventory.StockUpdateMessage;
import com.eatup.commercial.messaging.purchase.PurchaseMessage;
import com.eatup.commercial.repository.purchase.PurchaseRepository;
import com.eatup.commercial.service.purchase.PurchaseProcessorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseProcessorServiceImpl implements PurchaseProcessorService {

    private final PurchaseRepository purchaseRepository;
    private final InventoryMessagePublisher inventoryMessagePublisher;

    public PurchaseProcessorServiceImpl(PurchaseRepository purchaseRepository,
                                        InventoryMessagePublisher inventoryMessagePublisher) {
        this.purchaseRepository = purchaseRepository;
        this.inventoryMessagePublisher = inventoryMessagePublisher;
    }

    @Override
    @Transactional
    public void processPurchaseCreated(PurchaseMessage message) {

        if (purchaseRepository.existsByOrderNumber(message.getOrderNumber())) {

            LOGGER.warn(
                    "Purchase already processed with orderNumber: {}",
                    message.getOrderNumber()
            );

            return;
        }

        PurchaseDomain domain = new PurchaseDomain();
        domain.setOrderNumber(message.getOrderNumber());
        domain.setProviderId(message.getProviderId());
        domain.setLocationId(message.getLocationId());
        domain.setStatus(PurchaseStatus.CREATED);
        domain.setDeleted(false);
        domain.markAsCreated();

        List<PurchaseItemDomain> items = message.getItems().stream()
                .map(item -> {
                    PurchaseItemDomain itemDomain = new PurchaseItemDomain();
                    itemDomain.setProductId(item.getProductId());
                    itemDomain.setQuantity(item.getQuantity());
                    itemDomain.setUnitPrice(item.getUnitPrice());
                    itemDomain.initialize();
                    return itemDomain;
                })
                .toList();

        domain.replaceItems(items);
        try {
            purchaseRepository.save(domain);
        }
        catch (DataIntegrityViolationException ex) {

            LOGGER.warn(
                    "Duplicate purchase detected for orderNumber: {}",
                    message.getOrderNumber()
            );
        }
    }

    @Override
    @Transactional
    public void processPurchaseUpdated(PurchaseMessage message) {
        PurchaseDomain existing = findByIdOrThrow(message);
        existing.setProviderId(message.getProviderId());

        List<PurchaseItemDomain> newItems = message.getItems().stream()
                .map(item -> {
                    PurchaseItemDomain itemDomain = new PurchaseItemDomain();
                    itemDomain.setProductId(item.getProductId());
                    itemDomain.setQuantity(item.getQuantity());
                    itemDomain.setUnitPrice(item.getUnitPrice());
                    itemDomain.initialize();
                    return itemDomain;
                })
                .toList();

        existing.replaceItems(newItems);
        existing.markAsModified();
        purchaseRepository.save(existing);
    }

    @Override
    @Transactional
    public void processPurchaseStatusUpdated(PurchaseMessage message) {
        PurchaseDomain existing = findByIdOrThrow(message);
        boolean changed = existing.changeStatus(message.getStatus());
        if (!changed) {
            LOGGER.warn(
                    "Invalid purchase status transition from {} to {} for purchase {}",
                    existing.getStatus(),
                    message.getStatus(),
                    existing.getId()
            );
            return;
        }
        existing.markAsModified();
        purchaseRepository.save(existing);
        LOGGER.info(
                "Purchase status updated successfully to {} for purchase {}",
                message.getStatus(),
                existing.getId()
        );

        if (message.getStatus() == PurchaseStatus.RECEIVED) {
            existing.getItems().forEach(item -> {
                StockUpdateMessage stockMessage = new StockUpdateMessage(
                        existing.getLocationId(),
                        item.getProductId(),
                        item.getQuantity()
                );
                inventoryMessagePublisher.publishStockUpdate(stockMessage);
            });
            LOGGER.info("Stock update published for {} items on purchase {}",
                    existing.getItems().size(), existing.getId());
        }
    }

    @Override
    @Transactional
    public void processPurchaseDeleted(PurchaseMessage message) {
        PurchaseDomain existing = findByIdOrThrow(message);
        existing.softDelete();
        purchaseRepository.save(existing);
    }

    private PurchaseDomain findByIdOrThrow(PurchaseMessage message) {
        return purchaseRepository.findByIdAndLocationIdAndDeletedFalse(
                        message.getPurchaseId(), message.getLocationId())
                .orElseThrow(() -> new RuntimeException(
                        "Purchase not found with id: " + message.getPurchaseId()));
    }

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PurchaseProcessorServiceImpl.class);
}
