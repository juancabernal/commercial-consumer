package com.eatup.commercial.repository.table;

import com.eatup.commercial.domain.table.ReservationStatus;
import com.eatup.commercial.domain.table.TableReservationDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface TableReservationRepository extends JpaRepository<TableReservationDomain, UUID> {

    List<TableReservationDomain> findAllByTableIdOrderByReservationDateAscReservationTimeAsc(UUID tableId);

    List<TableReservationDomain> findAllByTableIdAndStatusInOrderByReservationDateAscReservationTimeAsc(
            UUID tableId,
            Collection<ReservationStatus> statuses
    );

    List<TableReservationDomain> findAllByGuestDocumentNumberAndStatusInOrderByReservationDateAscReservationTimeAsc(
            String guestDocumentNumber,
            Collection<ReservationStatus> statuses
    );
}
