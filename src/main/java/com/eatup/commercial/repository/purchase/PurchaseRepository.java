package com.eatup.commercial.repository.purchase;

import com.eatup.commercial.domain.purchase.PurchaseDomain;
import com.eatup.commercial.domain.purchase.PurchaseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PurchaseRepository extends JpaRepository<PurchaseDomain, UUID> {

    boolean existsByOrderNumber(String orderNumber);

    Page<PurchaseDomain> findByLocationIdAndDeletedFalse(UUID locationId, Pageable pageable);

    Page<PurchaseDomain> findByLocationIdAndStatusAndDeletedFalse(
            UUID locationId,
            PurchaseStatus status,
            Pageable pageable
    );

    Optional<PurchaseDomain> findByIdAndLocationIdAndDeletedFalse(UUID id, UUID locationId);
}
