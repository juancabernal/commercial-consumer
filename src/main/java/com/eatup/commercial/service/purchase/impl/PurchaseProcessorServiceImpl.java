package com.eatup.commercial.service.purchase.impl;

import com.eatup.commercial.domain.purchase.PurchaseDomain;
import com.eatup.commercial.domain.purchase.PurchaseItemDomain;
import com.eatup.commercial.domain.purchase.PurchaseStatus;
import com.eatup.commercial.messaging.purchase.PurchaseMessage;
import com.eatup.commercial.repository.purchase.PurchaseRepository;
import com.eatup.commercial.service.purchase.PurchaseProcessorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseProcessorServiceImpl implements PurchaseProcessorService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseProcessorServiceImpl(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    @Override
    @Transactional
    public void processPurchaseCreated(PurchaseMessage message) {
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
                    itemDomain.setProductName(item.getProductName());
                    itemDomain.setQuantity(item.getQuantity());
                    itemDomain.setUnitPrice(item.getUnitPrice());
                    itemDomain.initialize();
                    return itemDomain;
                })
                .toList();

        domain.replaceItems(items);
        purchaseRepository.save(domain);
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
                    itemDomain.setProductName(item.getProductName());
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
        existing.changeStatus(message.getStatus());
        existing.markAsModified();
        purchaseRepository.save(existing);
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
}
