package com.eatup.commercial.repository.customerDiscount;

import com.eatup.commercial.domain.customerDiscount.CustomerDiscountDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CustomerDiscountRepository extends JpaRepository<CustomerDiscountDomain, UUID> {}