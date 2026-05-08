package com.eatup.commercial.utils.discount.mapper;

import com.eatup.commercial.domain.discount.DiscountDomain;
import com.eatup.commercial.dto.discount.DiscountDTO;
import org.springframework.stereotype.Component;

@Component
public class DiscountMapper {

    public DiscountDomain toDomain(DiscountDTO dto) {
        DiscountDomain domain = new DiscountDomain();
        domain.setCategoryId(dto.getCategoryId());
        domain.setPercentage(dto.getPercentage());
        domain.setDescription(dto.getDescription());
        domain.setStatus(dto.getStatus());
        return domain;
    }

    public DiscountDTO toDto(DiscountDomain domain) {
        DiscountDTO dto = new DiscountDTO();
        dto.setId(domain.getId());
        dto.setCategoryId(domain.getCategoryId());
        dto.setPercentage(domain.getPercentage());
        dto.setDescription(domain.getDescription());
        dto.setStatus(domain.getStatus());
        return dto;
    }
}