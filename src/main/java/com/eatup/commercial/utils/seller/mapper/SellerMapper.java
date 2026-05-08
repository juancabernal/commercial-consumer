package com.eatup.commercial.utils.seller.mapper;

import com.eatup.commercial.domain.seller.SellerDomain;
import com.eatup.commercial.dto.seller.SellerDTO;
import org.springframework.stereotype.Component;

@Component
public class SellerMapper {

    public SellerDTO toDto(SellerDomain sellerDomain) {
        SellerDTO sellerDTO = new SellerDTO();
        sellerDTO.setId(sellerDomain.getId());
        sellerDTO.setDocumentTypeId(sellerDomain.getDocumentTypeId());
        sellerDTO.setLocationId(sellerDomain.getLocationId());
        sellerDTO.setIdentificationNumber(sellerDomain.getIdentificationNumber());
        sellerDTO.setFirstName(sellerDomain.getFirstName());
        sellerDTO.setLastName(sellerDomain.getLastName());
        sellerDTO.setPhone(sellerDomain.getPhone());
        sellerDTO.setEmail(sellerDomain.getEmail());
        sellerDTO.setCommissionPercentage(sellerDomain.getCommissionPercentage());
        sellerDTO.setStatus(sellerDomain.getStatus());
        sellerDTO.setCreatedDate(sellerDomain.getCreatedDate());
        sellerDTO.setModifiedDate(sellerDomain.getModifiedDate());
        return sellerDTO;
    }

    public SellerDomain toDomain(SellerDTO dto) {
        SellerDomain sellerDomain = new SellerDomain();
        sellerDomain.setId(dto.getId());
        sellerDomain.setDocumentTypeId(dto.getDocumentTypeId());
        sellerDomain.setLocationId(dto.getLocationId());
        sellerDomain.setIdentificationNumber(dto.getIdentificationNumber());
        sellerDomain.setFirstName(dto.getFirstName());
        sellerDomain.setLastName(dto.getLastName());
        sellerDomain.setPhone(dto.getPhone());
        sellerDomain.setEmail(dto.getEmail());
        sellerDomain.setCommissionPercentage(dto.getCommissionPercentage());
        sellerDomain.setStatus(dto.getStatus());
        sellerDomain.setCreatedDate(dto.getCreatedDate());
        sellerDomain.setModifiedDate(dto.getModifiedDate());
        return sellerDomain;
    }
}