package com.eatup.commercial.service.sale.impl;

import com.eatup.commercial.domain.sale.SaleDomain;
import com.eatup.commercial.domain.sale.SaleStatus;
import com.eatup.commercial.messaging.sales.SalePatchRequestedMessage;
import com.eatup.commercial.repository.sale.SaleRepository;
import com.eatup.commercial.service.sale.SaleService;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class SaleServiceImpl implements SaleService {

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
            throw new IllegalArgumentException("Patch message is incomplete");
        }

        SaleDomain sale = saleRepository.findById(message.getSaleId())
                .orElseThrow(() -> new IllegalArgumentException("Sale not found: " + message.getSaleId()));

        SaleStatus nextStatus = SaleStatus.valueOf(message.getRequest().getStatus().trim().toUpperCase());
        validateTransition(sale.getStatus(), nextStatus);

        sale.setStatus(nextStatus);
        sale.setModifiedDate(LocalDateTime.now());
        saleRepository.save(sale);
    }

    private void validateTransition(SaleStatus currentStatus, SaleStatus nextStatus) {
        if (Objects.equals(currentStatus, nextStatus)) {
            return;
        }

        EnumSet<SaleStatus> allowedTargets = ALLOWED_TRANSITIONS.getOrDefault(currentStatus, EnumSet.noneOf(SaleStatus.class));
        if (!allowedTargets.contains(nextStatus)) {
            throw new IllegalStateException("Invalid transition from " + currentStatus + " to " + nextStatus);
        }
    }
}
