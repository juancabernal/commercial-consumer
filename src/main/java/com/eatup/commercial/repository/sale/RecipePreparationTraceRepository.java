package com.eatup.commercial.repository.sale;

import com.eatup.commercial.domain.sale.RecipePreparationTraceDomain;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipePreparationTraceRepository extends JpaRepository<RecipePreparationTraceDomain, UUID> {
}
