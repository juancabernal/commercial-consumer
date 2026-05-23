package com.eatup.commercial.dto.provider;

import com.eatup.commercial.domain.provider.ProviderStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProviderDTO {

    private UUID id;
    private String businessName;
    private Long documentTypeId;
    private String documentNumber;
    private Long taxRegimeId;
    private String responsibleFirstName;
    private String responsibleLastName;
    private String phone;
    private String email;
    private Long departmentId;
    private Long cityId;
    private String address;
    private Long branchId;
    private ProviderStatus status;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ProviderDTO() {
        // Default constructor required for JSON deserialization
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }

    public Long getDocumentTypeId() { return documentTypeId; }
    public void setDocumentTypeId(Long documentTypeId) { this.documentTypeId = documentTypeId; }

    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }

    public Long getTaxRegimeId() { return taxRegimeId; }
    public void setTaxRegimeId(Long taxRegimeId) { this.taxRegimeId = taxRegimeId; }

    public String getResponsibleFirstName() { return responsibleFirstName; }
    public void setResponsibleFirstName(String responsibleFirstName) { this.responsibleFirstName = responsibleFirstName; }

    public String getResponsibleLastName() { return responsibleLastName; }
    public void setResponsibleLastName(String responsibleLastName) { this.responsibleLastName = responsibleLastName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public Long getCityId() { return cityId; }
    public void setCityId(Long cityId) { this.cityId = cityId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public ProviderStatus getStatus() { return status; }
    public void setStatus(ProviderStatus status) { this.status = status; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }

    public LocalDateTime getModifiedDate() { return modifiedDate; }
    public void setModifiedDate(LocalDateTime modifiedDate) { this.modifiedDate = modifiedDate; }
}