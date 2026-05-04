package com.eatup.commercial.repository.table;

import com.eatup.commercial.domain.table.TableSessionDomain;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TableSessionRepository extends JpaRepository<TableSessionDomain, UUID> {

    Optional<TableSessionDomain> findByTableIdAndClosedAtIsNull(UUID tableId);

    List<TableSessionDomain> findAllByTableIdOrderByOpenedAtDesc(UUID tableId);
}
