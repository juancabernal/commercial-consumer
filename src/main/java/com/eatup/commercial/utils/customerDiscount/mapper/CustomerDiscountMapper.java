package com.eatup.commercial.utils.customerDiscount.mapper;

import com.eatup.commercial.domain.customerDiscount.CustomerDiscountDomain;
import com.eatup.commercial.dto.customerDiscount.CustomerDiscountDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerDiscountMapper {

    public CustomerDiscountDomain toDomain(CustomerDiscountDTO dto) {
        CustomerDiscountDomain domain = new CustomerDiscountDomain();
        domain.setCustomerId(dto.getCustomerId());
        domain.setLocationId(dto.getLocationId());
        domain.setDiscountId(dto.getDiscountId());
        domain.setAssignedAt(dto.getAssignedAt());
        domain.setStartDate(dto.getStartDate());
        domain.setEndDate(dto.getEndDate());
        return domain;
    }

    public CustomerDiscountDTO toDto(CustomerDiscountDomain domain) {
        CustomerDiscountDTO dto = new CustomerDiscountDTO();
        dto.setId(domain.getId());
        dto.setCustomerId(domain.getCustomerId());
        dto.setLocationId(domain.getLocationId());
        dto.setDiscountId(domain.getDiscountId());
        dto.setAssignedAt(domain.getAssignedAt());
        dto.setStartDate(domain.getStartDate());
        dto.setEndDate(domain.getEndDate());
        return dto;
    }
}