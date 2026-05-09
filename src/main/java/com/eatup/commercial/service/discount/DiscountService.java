package com.eatup.commercial.service.discount;

import com.eatup.commercial.dto.discount.DiscountDTO;
import java.util.UUID;

public interface DiscountService {
    void createDiscount(DiscountDTO dto);
    void updateDiscount(UUID id, DiscountDTO dto);
    void updateDiscountStatus(UUID id, Boolean status);
    void deleteDiscount(UUID id);
}