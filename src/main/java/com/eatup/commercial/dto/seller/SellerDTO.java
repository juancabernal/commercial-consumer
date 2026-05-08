package com.eatup.commercial.dto.seller;

import com.eatup.commercial.domain.seller.SellerStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class SellerDTO {

    private UUID id;
    private UUID documentTypeId;
    private UUID locationId;
    private String identificationNumber;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private Double commissionPercentage;
    private SellerStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public SellerDTO() {
        // Default constructor required for JSON deserialization
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }


    public UUID getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(UUID documentTypeId) { this.documentTypeId = documentTypeId; }


    public UUID getLocationId() {
        return locationId;
    }

    public void setLocationId(UUID locationId) {
        this.locationId = locationId;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getCommissionPercentage() {
        return commissionPercentage;
    }

    public void setCommissionPercentage(Double commissionPercentage) {
        this.commissionPercentage = commissionPercentage;
    }

    public SellerStatus getStatus() {
        return status;
    }

    public void setStatus(SellerStatus status) {
        this.status = status;
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
