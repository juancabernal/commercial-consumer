package com.eatup.commercial.utils.provider.mapper;

import com.eatup.commercial.domain.provider.ProviderDomain;
import com.eatup.commercial.dto.provider.ProviderDTO;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDTO toDto(ProviderDomain domain) {
        ProviderDTO dto = new ProviderDTO();
        dto.setId(domain.getId());
        dto.setBusinessName(domain.getBusinessName());
        dto.setDocumentTypeId(domain.getDocumentTypeId());
        dto.setDocumentNumber(domain.getDocumentNumber());
        dto.setTaxRegimeId(domain.getTaxRegimeId());
        dto.setResponsibleFirstName(domain.getResponsibleFirstName());
        dto.setResponsibleLastName(domain.getResponsibleLastName());
        dto.setPhone(domain.getPhone());
        dto.setEmail(domain.getEmail());
        dto.setDepartmentId(domain.getDepartmentId());
        dto.setCityId(domain.getCityId());
        dto.setAddress(domain.getAddress());
        dto.setBranchId(domain.getBranchId());
        dto.setStatus(domain.getStatus());
        dto.setCreatedDate(domain.getCreatedDate());
        dto.setModifiedDate(domain.getModifiedDate());
        return dto;
    }

    public ProviderDomain toDomain(ProviderDTO dto) {
        ProviderDomain domain = new ProviderDomain();
        domain.setId(dto.getId());
        domain.setBusinessName(dto.getBusinessName());
        domain.setDocumentTypeId(dto.getDocumentTypeId());
        domain.setDocumentNumber(dto.getDocumentNumber());
        domain.setTaxRegimeId(dto.getTaxRegimeId());
        domain.setResponsibleFirstName(dto.getResponsibleFirstName());
        domain.setResponsibleLastName(dto.getResponsibleLastName());
        domain.setPhone(dto.getPhone());
        domain.setEmail(dto.getEmail());
        domain.setDepartmentId(dto.getDepartmentId());
        domain.setCityId(dto.getCityId());
        domain.setAddress(dto.getAddress());
        domain.setBranchId(dto.getBranchId());
        domain.setStatus(dto.getStatus());
        domain.setCreatedDate(dto.getCreatedDate());
        domain.setModifiedDate(dto.getModifiedDate());
        return domain;
    }
}