package com.eatup.commercial.domain.provider;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "providers")
public class ProviderDomain {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "document_type_id", nullable = false)
    private Long documentTypeId;

    @Column(name = "document_number", nullable = false, unique = true, length = 20)
    private String documentNumber;

    @Column(name = "tax_regime_id", nullable = false)
    private Long taxRegimeId;

    @Column(name = "responsible_first_name", nullable = false, length = 100)
    private String responsibleFirstName;

    @Column(name = "responsible_last_name", nullable = false, length = 100)
    private String responsibleLastName;

    @Column(name = "phone", nullable = false, unique = true, length = 10)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    @Column(name = "city_id", nullable = false)
    private Long cityId;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "branch_id", nullable = false)
    private Long branchId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProviderStatus status;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_date", nullable = false)
    private LocalDateTime modifiedDate;

    public ProviderDomain() {
        // Default constructor required by JPA
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