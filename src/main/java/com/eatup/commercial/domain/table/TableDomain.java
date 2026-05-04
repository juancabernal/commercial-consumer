package com.eatup.commercial.domain.table;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "restaurant_tables",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_restaurant_tables_venue_number_active", columnNames = {"venueId", "tableNumber", "active"})
        },
        indexes = {
                @Index(name = "idx_restaurant_tables_venue", columnList = "venueId"),
                @Index(name = "idx_restaurant_tables_venue_number_active", columnList = "venueId, tableNumber, active")
        }
)
public class TableDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer tableNumber;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(nullable = false)
    private Boolean isVip;

    @Column(nullable = false)
    private Boolean hasView;

    @Column(nullable = false)
    private Boolean isAccessible;

    @Column(nullable = false)
    private UUID venueId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TableStatus status;

    @Column(nullable = false)
    private Boolean active;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @Column(nullable = false)
    private LocalDateTime modifiedDate;
}
