package com.eatup.commercial.service.discount.impl;

import com.eatup.commercial.domain.discount.DiscountDomain;
import com.eatup.commercial.dto.discount.DiscountDTO;
import com.eatup.commercial.repository.discount.DiscountRepository;
import com.eatup.commercial.service.discount.DiscountService;
import com.eatup.commercial.utils.discount.exceptions.DiscountResourceNotFoundException;
import com.eatup.commercial.utils.discount.mapper.DiscountMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {

    private static final Logger log = LoggerFactory.getLogger(DiscountServiceImpl.class);

    private final DiscountRepository discountRepository;
    private final DiscountMapper discountMapper;

    public DiscountServiceImpl(DiscountRepository discountRepository, DiscountMapper discountMapper) {
        this.discountRepository = discountRepository;
        this.discountMapper = discountMapper;
    }

    @Override
    @Transactional
    public void createDiscount(DiscountDTO dto) {
        log.info("Creando descuento: categoryId={}, percentage={}", dto.getCategoryId(), dto.getPercentage());
        DiscountDomain domain = discountMapper.toDomain(dto);
        discountRepository.save(domain);
        log.info("Descuento creado exitosamente. id={}, description={}", domain.getId(), domain.getDescription());
    }

    @Override
    @Transactional
    public void updateDiscount(UUID id, DiscountDTO dto) {
        log.info("Actualizando descuento: id={}", id);
        DiscountDomain domain = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id));
        domain.setCategoryId(dto.getCategoryId());
        domain.setPercentage(dto.getPercentage());
        domain.setDescription(dto.getDescription());
        domain.setStatus(dto.getStatus());
        discountRepository.save(domain);
        log.info("Descuento actualizado exitosamente. id={}", id);
    }

    @Override
    @Transactional
    public void updateDiscountStatus(UUID id, Boolean status) {
        log.info("Actualizando estado de descuento: id={}, status={}", id, status);
        DiscountDomain domain = discountRepository.findById(id)
                .orElseThrow(() -> new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id));
        domain.setStatus(status);
        discountRepository.save(domain);
        log.info("Estado de descuento actualizado exitosamente. id={}, status={}", id, status);
    }

    @Override
    @Transactional
    public void deleteDiscount(UUID id) {
        log.info("Eliminando descuento: id={}", id);
        if (!discountRepository.existsById(id)) {
            throw new DiscountResourceNotFoundException("Descuento no encontrado con id: " + id);
        }
        discountRepository.deleteById(id);
        log.info("Descuento eliminado exitosamente. id={}", id);
    }
}