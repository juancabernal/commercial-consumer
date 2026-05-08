package com.eatup.commercial.service.customerDiscount.impl;

import com.eatup.commercial.domain.customerDiscount.CustomerDiscountDomain;
import com.eatup.commercial.dto.customerDiscount.CustomerDiscountDTO;
import com.eatup.commercial.repository.customerDiscount.CustomerDiscountRepository;
import com.eatup.commercial.service.customerDiscount.CustomerDiscountService;
import com.eatup.commercial.utils.customerDiscount.exceptions.CustomerDiscountResourceNotFoundException;
import com.eatup.commercial.utils.customerDiscount.mapper.CustomerDiscountMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class CustomerDiscountServiceImpl implements CustomerDiscountService {

    private final CustomerDiscountRepository customerDiscountRepository;
    private final CustomerDiscountMapper customerDiscountMapper;

    public CustomerDiscountServiceImpl(CustomerDiscountRepository customerDiscountRepository,
                                       CustomerDiscountMapper customerDiscountMapper) {
        this.customerDiscountRepository = customerDiscountRepository;
        this.customerDiscountMapper = customerDiscountMapper;
    }

    @Override
    public void createCustomerDiscount(CustomerDiscountDTO dto) {
        CustomerDiscountDomain domain = customerDiscountMapper.toDomain(dto);
        domain.setCreatedAt(LocalDateTime.now());
        customerDiscountRepository.save(domain);
    }

    @Override
    public void updateCustomerDiscount(UUID id, CustomerDiscountDTO dto) {
        CustomerDiscountDomain domain = customerDiscountRepository.findById(id)
                .orElseThrow(() -> new CustomerDiscountResourceNotFoundException("Descuento de cliente no encontrado con id: " + id));
        domain.setCustomerId(dto.getCustomerId());
        domain.setLocationId(dto.getLocationId());
        domain.setDiscountId(dto.getDiscountId());
        domain.setAssignedAt(dto.getAssignedAt());
        domain.setStartDate(dto.getStartDate());
        domain.setEndDate(dto.getEndDate());
        domain.setModifiedAt(LocalDateTime.now());
        customerDiscountRepository.save(domain);
    }

    @Override
    public void deleteCustomerDiscount(UUID id) {
        if (!customerDiscountRepository.existsById(id)) {
            throw new CustomerDiscountResourceNotFoundException("Descuento de cliente no encontrado con id: " + id);
        }
        customerDiscountRepository.deleteById(id);
    }
}
