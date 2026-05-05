package com.eatup.commercial.dto.table;

import com.eatup.commercial.domain.table.ReservationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TableReservationDTO {

    @Null(message = "El campo 'id' de la reserva es administrado por el servidor y no debe enviarse")
    private String id;

    @Null(message = "El campo 'tableId' de la reserva se toma del path y no debe enviarse en el body")
    private String tableId;

    @NotNull(message = "El campo 'reservationDate' es requerido")
    private LocalDate reservationDate;

    @NotNull(message = "El campo 'reservationTime' es requerido")
    private LocalTime reservationTime;

    @Null(message = "El campo 'reservationDateTime' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime reservationDateTime;

    @Null(message = "El campo 'reservationLockStartsAt' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime reservationLockStartsAt;

    @Null(message = "El campo 'reservationGraceEndsAt' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime reservationGraceEndsAt;

    @NotBlank(message = "El campo 'guestName' es requierido y no puede estar vacio")
    @Size(max = 100, message = "El campo 'guestName' no puede superar los 100 caracteres")
    private String guestName;

    @NotBlank(message = "El campo 'guestDocumentNumber' es requerido y no puede estar vacio")
    @Size(max = 50, message = "El campo 'guestDocumentNumber' no puede superar los 50 caracteres")
    private String guestDocumentNumber;

    @NotNull(message = "El campo 'guestCount' de la reserva es requerido")
    @Positive(message = "El campo 'guestCount' de la reserva debe ser un numero positivo mayor que cero")
    private Integer guestCount;

    @Null(message = "El campo 'status' de la reserva es administrado por el servidor y no puede enviarse manualmente")
    private ReservationStatus status;

    @Null(message = "El campo 'createdDate' de la reserva es administrado por el servidor y no debe enviarse")
    private LocalDateTime createdDate;

    public TableReservationDTO() {
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

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public LocalDateTime getReservationDateTime() {
        return reservationDateTime;
    }

    public void setReservationDateTime(LocalDateTime reservationDateTime) {
        this.reservationDateTime = reservationDateTime;
    }

    public LocalDateTime getReservationLockStartsAt() {
        return reservationLockStartsAt;
    }

    public void setReservationLockStartsAt(LocalDateTime reservationLockStartsAt) {
        this.reservationLockStartsAt = reservationLockStartsAt;
    }

    public LocalDateTime getReservationGraceEndsAt() {
        return reservationGraceEndsAt;
    }

    public void setReservationGraceEndsAt(LocalDateTime reservationGraceEndsAt) {
        this.reservationGraceEndsAt = reservationGraceEndsAt;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        this.guestCount = guestCount;
    }

    public String getGuestDocumentNumber() {
        return guestDocumentNumber;
    }

    public void setGuestDocumentNumber(String guestDocumentNumber) {
        this.guestDocumentNumber = guestDocumentNumber;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
