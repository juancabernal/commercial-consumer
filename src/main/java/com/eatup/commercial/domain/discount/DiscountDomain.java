package com.eatup.commercial.domain.discount;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "discounts")
public class DiscountDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private Integer percentage;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime modifiedAt;

    public DiscountDomain() {}
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getCategoryId() { return categoryId; }
    public void setCategoryId(UUID categoryId) { this.categoryId = categoryId; }
    public Integer getPercentage() { return percentage; }
    public void setPercentage(Integer percentage) { this.percentage = percentage; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getStatus() { return status; }
    public void setStatus(Boolean status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getModifiedAt() { return modifiedAt; }
    public void setModifiedAt(LocalDateTime modifiedAt) { this.modifiedAt = modifiedAt; }
}