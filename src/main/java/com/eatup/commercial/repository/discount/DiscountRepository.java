package com.eatup.commercial.repository.discount;

import com.eatup.commercial.domain.discount.DiscountDomain;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<DiscountDomain, UUID> {}