package com.eatup.commercial.dto.table;

public class TableSummaryDTO {

    private Long totalRegistered;
    private Long available;
    private Long occupied;
    private Long reserved;
    private Long blockedForReservation;
    private String venueId;

    public TableSummaryDTO() {
    }

    public Long getTotalRegistered() {
        return totalRegistered;
    }

    public void setTotalRegistered(Long totalRegistered) {
        this.totalRegistered = totalRegistered;
    }

    public Long getAvailable() {
        return available;
    }

    public void setAvailable(Long available) {
        this.available = available;
    }

    public Long getOccupied() {
        return occupied;
    }

    public void setOccupied(Long occupied) {
        this.occupied = occupied;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public Long getBlockedForReservation() {
        return blockedForReservation;
    }

    public void setBlockedForReservation(Long blockedForReservation) {
        this.blockedForReservation = blockedForReservation;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }
}
