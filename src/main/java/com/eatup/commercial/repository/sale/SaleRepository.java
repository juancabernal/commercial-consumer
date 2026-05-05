package com.eatup.commercial.repository.sale;

import com.eatup.commercial.domain.sale.SaleDomain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<SaleDomain, UUID> {
}
