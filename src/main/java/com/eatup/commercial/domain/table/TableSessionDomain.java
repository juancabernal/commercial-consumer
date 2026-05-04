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
        name = "table_sessions",
        indexes = {
                @Index(name = "idx_table_sessions_table_closed", columnList = "tableId, closedAt"),
                @Index(name = "idx_table_sessions_reservation", columnList = "reservationId")
        }
)
public class TableSessionDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID tableId;

    @Column
    private UUID reservationId;

    @Column(nullable = false)
    private Integer guestCount;

    @Column
    private UUID waiterId;

    @Column(nullable = false)
    private LocalDateTime openedAt;

    @Column
    private LocalDateTime closedAt;

    @Column
    private Long durationMinutes;

    @Column(length = 500)
    private String observations;
}
