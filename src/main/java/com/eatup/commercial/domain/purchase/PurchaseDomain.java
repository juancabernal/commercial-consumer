package com.eatup.commercial.domain.purchase;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "purchase")
public class PurchaseDomain {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "order_number", nullable = false, unique = true, length = 64)
    private String orderNumber;

    @Column(name = "provider_id", nullable = false)
    private UUID providerId;

    @Column(name = "location_id", nullable = false)
    private UUID locationId;

    @OneToMany(mappedBy = "purchase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseItemDomain> items = new ArrayList<>();

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PurchaseStatus status;

    @Column(nullable = false)
    private boolean deleted;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public boolean changeStatus(PurchaseStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            return false;
        }
        this.status = newStatus;
        return true;
    }

    public void replaceItems(List<PurchaseItemDomain> newItems) {
        this.items.clear();
        if (newItems != null) {
            for (PurchaseItemDomain item : newItems) {
                item.setPurchase(this);
                item.recalculateSubtotal();
                this.items.add(item);
            }
        }
        recalculateTotal();
    }

    public void recalculateTotal() {
        this.total = items.stream()
                .map(PurchaseItemDomain::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void softDelete() {
        this.deleted = true;
        this.modifiedDate = LocalDateTime.now();
    }

    public void markAsModified() {
        this.modifiedDate = LocalDateTime.now();
    }

    public void markAsCreated() {
        this.createdDate = LocalDateTime.now();
        this.modifiedDate = LocalDateTime.now();
    }
}
