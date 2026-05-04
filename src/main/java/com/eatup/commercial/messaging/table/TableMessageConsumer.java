package com.eatup.commercial.messaging.table;

import com.eatup.commercial.dto.table.TableDTO;
import com.eatup.commercial.dto.table.TableReservationDTO;
import com.eatup.commercial.dto.table.TableSessionDTO;
import com.eatup.commercial.service.table.TableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TableMessageConsumer {

    private final TableService tableService;

    @RabbitListener(queues = "${rabbitmq.queue.table}")
    public void consume(TableCommandEvent event) {
        switch (event.getEventType()) {
            case "TABLE_CREATED" ->
                    tableService.createTable(toTableDto(event.getPayload()));

            case "TABLE_UPDATED" ->
                    tableService.updateTable(event.getTableId(), toTableDto(event.getPayload()));

            case "TABLE_DEACTIVATED" ->
                    tableService.deactivateTable(event.getTableId());

            case "TABLE_SESSION_OPENED" ->
                    tableService.openSession(event.getTableId(), toSessionDto(event.getPayload()));

            case "TABLE_SESSION_UPDATED" ->
                    tableService.updateGuestCount(
                            event.getTableId(),
                            event.getSessionId(),
                            toInteger(value(event.getPayload(), "guestCount"))
                    );

            case "TABLE_SESSION_CLOSED" ->
                    tableService.closeSession(event.getTableId(), event.getSessionId());

            case "TABLE_RESERVATION_CREATED" ->
                    tableService.createReservation(event.getTableId(), toReservationDto(event.getPayload()));

            case "TABLE_RESERVATION_UPDATED" ->
                    tableService.updateReservation(
                            event.getTableId(),
                            event.getReservationId(),
                            toReservationDto(event.getPayload())
                    );

            case "TABLE_RESERVATION_CANCELLED" ->
                    tableService.cancelReservation(event.getTableId(), event.getReservationId());

            case "TABLE_RESERVATION_SEATED" ->
                    tableService.seatReservation(event.getReservationId(), toSessionDto(event.getPayload()));

            default ->
                    log.warn("Evento de table no procesado por commercial-service: {}", event.getEventType());
        }
    }

    private TableDTO toTableDto(Map<String, Object> payload) {
        TableDTO dto = new TableDTO();
        dto.setTableNumber(toInteger(value(payload, "tableNumber")));
        dto.setLocation(toString(value(payload, "location")));
        dto.setIsVip(toBoolean(value(payload, "isVip")));
        dto.setHasView(toBoolean(value(payload, "hasView")));
        dto.setIsAccessible(toBoolean(value(payload, "isAccessible")));
        dto.setVenueId(toString(value(payload, "venueId")));
        return dto;
    }

    private TableSessionDTO toSessionDto(Map<String, Object> payload) {
        TableSessionDTO dto = new TableSessionDTO();
        dto.setReservationId(toString(value(payload, "reservationId")));
        dto.setGuestCount(toInteger(value(payload, "guestCount")));
        dto.setWaiterId(toString(value(payload, "waiterId")));
        dto.setObservations(toString(value(payload, "observations")));
        return dto;
    }

    private TableReservationDTO toReservationDto(Map<String, Object> payload) {
        TableReservationDTO dto = new TableReservationDTO();
        dto.setReservationDate(toLocalDate(value(payload, "reservationDate")));
        dto.setReservationTime(toLocalTime(value(payload, "reservationTime")));
        dto.setGuestName(toString(value(payload, "guestName")));
        dto.setGuestDocumentNumber(toString(value(payload, "guestDocumentNumber")));
        dto.setGuestCount(toInteger(value(payload, "guestCount")));
        return dto;
    }

    private Object value(Map<String, Object> payload, String key) {
        return payload != null ? payload.get(key) : null;
    }

    private String toString(Object value) {
        return value != null ? value.toString() : null;
    }

    private Integer toInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number number) {
            return number.intValue();
        }
        return Integer.valueOf(value.toString());
    }

    private Boolean toBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean bool) {
            return bool;
        }
        return Boolean.valueOf(value.toString());
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        return LocalDate.parse(value.toString());
    }

    private LocalTime toLocalTime(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalTime localTime) {
            return localTime;
        }
        return LocalTime.parse(value.toString());
    }
}
