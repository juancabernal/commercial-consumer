package com.eatup.commercial.service.discount.impl;

import com.eatup.commercial.domain.discount.DiscountDomain;
import com.eatup.commercial.dto.discount.DiscountDTO;
import com.eatup.commercial.repository.discount.DiscountRepository;
import com.eatup.commercial.service.discount.DiscountService;
import com.eatup.commercial.utils.discount.exceptions.DiscountResourceNotFoundException;
import com.eatup.commercial.utils.discount.mapper.DiscountMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    public DiscountServiceImpl(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    @Override
    public void createDiscount(DiscountDTO dto) {
        DiscountDomain domain = discountMapper.toDomain(dto);
        domain.setCreatedAt(LocalDateTime.now());
        discountRepository.save(domain);
    }

    @Override
    public void updateDiscount(UUID id, DiscountDTO dto) {
        DiscountDomain domain = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id));
        domain.setCategoryId(dto.getCategoryId());
        domain.setPercentage(dto.getPercentage());
        domain.setDescription(dto.getDescription());
        domain.setStatus(dto.getStatus());
        domain.setModifiedAt(LocalDateTime.now());
        discountRepository.save(domain);
    }

    @Override
    public void updateDiscountStatus(UUID id, Boolean status) {
        DiscountDomain domain = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id));
        domain.setStatus(status);
        domain.setModifiedAt(LocalDateTime.now());
        discountRepository.save(domain);
    }

    @Override
    public void deleteDiscount(UUID id) {
        if (!discountRepository.existsById(id)) {
            throw new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id);
        }
        discountRepository.deleteById(id);
    }
}