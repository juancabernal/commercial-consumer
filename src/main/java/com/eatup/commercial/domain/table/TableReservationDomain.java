package com.eatup.commercial.domain.table;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "table_reservations",
        indexes = {
                @Index(name = "idx_table_reservations_table_status", columnList = "tableId, status"),
                @Index(name = "idx_table_reservations_table_schedule", columnList = "tableId, reservationDate, reservationTime")
        }
)
public class TableReservationDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tableId;

    @Column(nullable = false)
    private LocalDate reservationDate;

    @Column(nullable = false)
    private LocalTime reservationTime;

    @Column(nullable = false, length = 100)
    private String guestName;

    @Column(nullable = false, length = 50)
    private String guestDocumentNumber;

    @Column
    private Integer guestCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservationStatus status;

    @Column(nullable = false)
    private LocalDateTime createdDate;
}
