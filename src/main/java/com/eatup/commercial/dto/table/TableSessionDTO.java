package com.eatup.commercial.dto.table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TableSessionDTO {

    private static final String UUID_REGEX =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

    @Null(message = "El campo 'id' de la sesión es administrado por el servidor y no debe enviarse")
    private String id;

    @Null(message = "El campo 'tableId' de la sesión se toma del path y no debe enviarse en el body")
    private String tableId;

    @Pattern(
            regexp = UUID_REGEX,
            message = "El campo 'reservationId' debe ser un UUID válido"
    )
    private String reservationId;

    @NotNull(message = "El campo 'guestCount' es requerido")
    @Positive(message = "El campo 'guestCount' debe ser un número positivo mayor que cero")
    private Integer guestCount;

    @Pattern(
            regexp = UUID_REGEX,
            message = "El campo 'waiterId' debe ser un UUID válido"
    )
    private String waiterId;

    @Null(message = "El campo 'openedAt' es administrado por el servidor y no debe enviarse")
    private LocalDateTime openedAt;

    @Null(message = "El campo 'closedAt' es administrado por el servidor y no debe enviarse")
    private LocalDateTime closedAt;

    @Null(message = "El campo 'durationMinutes' es administrado por el servidor y no debe enviarse")
    private Long durationMinutes;

    @Null(message = "El campo 'durationText' es administrado por el servidor y no debe enviarse")
    private String durationText;

    @Size(max = 500, message = "El campo 'observations' no puede superar los 500 caracteres")
    private String observations;

    public TableSessionDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTableId() {
        return tableId;
    }

    public void setTableId(String tableId) {
        this.tableId = tableId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getWaiterId() {
        return waiterId;
    }

    public void setWaiterId(String waiterId) {
        this.waiterId = waiterId;
    }

    public LocalDateTime getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(LocalDateTime openedAt) {
        this.openedAt = openedAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public Long getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Long durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}