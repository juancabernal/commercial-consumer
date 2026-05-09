package com.eatup.commercial.dto.discount;

import java.util.UUID;

public class DiscountDTO {
    private UUID id;
    private UUID categoryId;
    private Integer percentage;
    private String description;
    private Boolean status;

    public DiscountDTO() {}
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
}