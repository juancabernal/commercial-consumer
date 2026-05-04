package com.eatup.commercial.domain.sale;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
