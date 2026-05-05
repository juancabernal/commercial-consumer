package com.eatup.commercial.dto.table;

import com.eatup.commercial.domain.table.TableStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class TableDTO {

    private static final String UUID_REGEX =
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89aAbB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$";

    @Null(message = "El campo 'id' es administrado por el servidor y no debe enviarse")
    private String id;

    @NotNull(message = "El campo 'tableNumber' es requerido")
    @Positive(message = "El campo 'tableNumber' debe ser un número positivo mayor que cero")
    private Integer tableNumber;

    @NotBlank(message = "El campo 'location' es requerido y no puede estar vacío")
    @Size(max = 100, message = "El campo 'location' no puede superar los 100 caracteres")
    private String location;

    private Boolean isVip;
    private Boolean hasView;
    private Boolean isAccessible;

    @NotBlank(message = "El campo 'venueId' es requerido y no puede estar vacío")
    @Pattern(
            regexp = UUID_REGEX,
            message = "El campo 'venueId' debe ser un UUID válido"
    )
    private String venueId;

    @Null(message = "El campo 'status' es administrado por el servidor y no puede enviarse manualmente")
    private TableStatus status;

    @Null(message = "El campo 'active' es administrado por el servidor y no puede enviarse manualmente")
    private Boolean active;

    @Null(message = "El campo 'reserved' es calculado por el sistema y no puede enviarse manualmente")
    private Boolean reserved;

    @Null(message = "El campo 'canOpenNow' es calculado por el sistema y no puede enviarse manualmente")
    private Boolean canOpenNow;

    @Null(message = "El campo 'canOpenWithReservation' es calculado por el sistema y no puede enviarse manualmente")
    private Boolean canOpenWithReservation;

    @Null(message = "El campo 'displayStatus' es calculado por el sistema y no puede enviarse manualmente")
    private String displayStatus;

    @Null(message = "El campo 'activeSession' es calculado por el sistema y no puede enviarse manualmente")
    private TableSessionDTO activeSession;

    @Null(message = "El campo 'activeReservation' es calculado por el sistema y no puede enviarse manualmente")
    private TableReservationDTO activeReservation;

    @Null(message = "El campo 'nextReservationAt' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime nextReservationAt;

    @Null(message = "El campo 'reservationLockStartsAt' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime reservationLockStartsAt;

    @Null(message = "El campo 'reservationGraceEndsAt' es calculado por el sistema y no puede enviarse manualmente")
    private LocalDateTime reservationGraceEndsAt;

    @Null(message = "El campo 'createdDate' es administrado por el servidor y no debe enviarse")
    private LocalDateTime createdDate;

    @Null(message = "El campo 'modifiedDate' es administrado por el servidor y no debe enviarse")
    private LocalDateTime modifiedDate;

    public TableDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getIsVip() {
        return isVip;
    }

    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public Boolean getHasView() {
        return hasView;
    }

    public void setHasView(Boolean hasView) {
        this.hasView = hasView;
    }

    public Boolean getIsAccessible() {
        return isAccessible;
    }

    public void setIsAccessible(Boolean isAccessible) {
        this.isAccessible = isAccessible;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public TableStatus getStatus() {
        return status;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getReserved() {
        return reserved;
    }

    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    public Boolean getCanOpenNow() {
        return canOpenNow;
    }

    public void setCanOpenNow(Boolean canOpenNow) {
        this.canOpenNow = canOpenNow;
    }

    public Boolean getCanOpenWithReservation() {
        return canOpenWithReservation;
    }

    public void setCanOpenWithReservation(Boolean canOpenWithReservation) {
        this.canOpenWithReservation = canOpenWithReservation;
    }

    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    public TableSessionDTO getActiveSession() {
        return activeSession;
    }

    public void setActiveSession(TableSessionDTO activeSession) {
        this.activeSession = activeSession;
    }

    public TableReservationDTO getActiveReservation() {
        return activeReservation;
    }

    public void setActiveReservation(TableReservationDTO activeReservation) {
        this.activeReservation = activeReservation;
    }

    public LocalDateTime getNextReservationAt() {
        return nextReservationAt;
    }

    public void setNextReservationAt(LocalDateTime nextReservationAt) {
        this.nextReservationAt = nextReservationAt;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(LocalDateTime modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}