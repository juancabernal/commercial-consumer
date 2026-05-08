package com.eatup.commercial.service.customerDiscount;

import com.eatup.commercial.dto.customerDiscount.CustomerDiscountDTO;
import java.util.UUID;

public interface CustomerDiscountService {
    void createCustomerDiscount(CustomerDiscountDTO dto);
    void updateCustomerDiscount(UUID id, CustomerDiscountDTO dto);
    void deleteCustomerDiscount(UUID id);
}