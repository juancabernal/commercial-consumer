package com.eatup.commercial.service.customerDiscount.impl;

import com.eatup.commercial.domain.customerDiscount.CustomerDiscountDomain;
import com.eatup.commercial.dto.customerDiscount.CustomerDiscountDTO;
import com.eatup.commercial.repository.customerDiscount.CustomerDiscountRepository;
import com.eatup.commercial.service.customerDiscount.CustomerDiscountService;
import com.eatup.commercial.utils.customerDiscount.exceptions.CustomerDiscountResourceNotFoundException;
import com.eatup.commercial.utils.customerDiscount.mapper.CustomerDiscountMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerDiscountServiceImpl implements CustomerDiscountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDiscountServiceImpl.class);

    private final CustomerDiscountRepository customerDiscountRepository;
    private final CustomerDiscountMapper customerDiscountMapper;

    public CustomerDiscountServiceImpl(CustomerDiscountRepository customerDiscountRepository,
                                       CustomerDiscountMapper customerDiscountMapper) {
        this.customerDiscountRepository = customerDiscountRepository;
        this.customerDiscountMapper = customerDiscountMapper;
    }

    @Override
    @Transactional
    public void createCustomerDiscount(CustomerDiscountDTO dto) {
        LOGGER.info("Creando descuento de cliente: customerId={}, discountId={}, locationId={}",
                dto.getCustomerId(), dto.getDiscountId(), dto.getLocationId());
        CustomerDiscountDomain domain = customerDiscountMapper.toDomain(dto);
        customerDiscountRepository.save(domain);
        LOGGER.info("Descuento de cliente creado exitosamente");
    }

    @Override
    @Transactional
    public void updateCustomerDiscount(UUID id, CustomerDiscountDTO dto) {
        LOGGER.info("Actualizando descuento de cliente: id={}", id);
        CustomerDiscountDomain domain = customerDiscountRepository.findById(id)
                .orElseThrow(() -> new CustomerDiscountResourceNotFoundException(
                        "Descuento de cliente no encontrado con id: " + id));
        domain.setCustomerId(dto.getCustomerId());
        domain.setLocationId(dto.getLocationId());
        domain.setDiscountId(dto.getDiscountId());
        domain.setAssignedAt(dto.getAssignedAt());
        domain.setStartDate(dto.getStartDate());
        domain.setEndDate(dto.getEndDate());
        customerDiscountRepository.save(domain);
        LOGGER.info("Descuento de cliente actualizado exitosamente: id={}", id);
    }

    @Override
    @Transactional
    public void deleteCustomerDiscount(UUID id) {
        LOGGER.info("Eliminando descuento de cliente: id={}", id);
        if (!customerDiscountRepository.existsById(id)) {
            throw new CustomerDiscountResourceNotFoundException(
                    "Descuento de cliente no encontrado con id: " + id);
        }
        customerDiscountRepository.deleteById(id);
        LOGGER.info("Descuento de cliente eliminado exitosamente: id={}", id);
    }
}
