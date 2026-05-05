package com.eatup.commercial.repository.table;

import com.eatup.commercial.domain.table.TableDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TableRepository extends JpaRepository<TableDomain, UUID> {

    List<TableDomain> findAllByActiveTrue();

    List<TableDomain> findAllByActiveTrueAndVenueId(UUID venueId);

    Optional<TableDomain> findByIdAndActiveTrue(UUID id);

    List<TableDomain> findAllByTableNumberAndActiveTrue(Integer tableNumber);

    boolean existsByVenueIdAndTableNumberAndActiveTrue(UUID venueId, Integer tableNumber);

    boolean existsByVenueIdAndTableNumberAndActiveTrueAndIdNot(UUID venueId, Integer tableNumber, UUID id);
}
