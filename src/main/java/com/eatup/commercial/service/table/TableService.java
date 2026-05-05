package com.eatup.commercial.service.table;

import com.eatup.commercial.dto.table.TableDTO;
import com.eatup.commercial.dto.table.TableReservationDTO;
import com.eatup.commercial.dto.table.TableSessionDTO;
import com.eatup.commercial.dto.table.TableSummaryDTO;

import java.util.List;

public interface TableService {

    TableDTO createTable(TableDTO request);
    List<TableDTO> getTables(String status, String venueId, Boolean reserved, Boolean canOpenNow);
    TableDTO getTableById(String tableId);
    List<TableDTO> getTablesByVenue(String venueId);
    TableDTO updateTable(String tableId, TableDTO request);
    void deactivateTable(String tableId);

    TableSessionDTO openSession(String tableId, TableSessionDTO request);
    TableSessionDTO getActiveSession(String tableId);
    TableSessionDTO updateGuestCount(String tableId, String sessionId, Integer guestCount);
    TableSessionDTO closeSession(String tableId, String sessionId);
    List<TableSessionDTO> getSessionHistory(String tableId);

    TableReservationDTO createReservation(String tableId, TableReservationDTO request);
    TableReservationDTO getActiveReservation(String tableId);
    TableReservationDTO updateReservation(String tableId, String reservationId, TableReservationDTO request);
    void cancelReservation(String tableId, String reservationId);
    List<TableReservationDTO> searchReservationsByGuestDocumentNumber(String guestDocumentNumber);
    TableSessionDTO seatReservation(String reservationId, TableSessionDTO request);

    TableSummaryDTO getSummary();
    TableSummaryDTO getSummaryByVenue(String venueId);
}
