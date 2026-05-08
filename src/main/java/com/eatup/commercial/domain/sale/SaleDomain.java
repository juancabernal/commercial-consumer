package com.eatup.commercial.domain.sale;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sales")
public class SaleDomain {
    @Id
    private UUID id;
    private String sellerId;
    private UUID locationId;
    private String tableId;
    @Enumerated(EnumType.STRING)
    private SaleStatus status;
    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SaleDetailDomain> details = new ArrayList<>();

    public void addDetail(SaleDetailDomain detail) {
        detail.setSale(this);
        this.details.add(detail);
    }
}
