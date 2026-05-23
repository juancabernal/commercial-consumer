package com.eatup.commercial.repository.provider;

import com.eatup.commercial.domain.provider.ProviderDomain;
import com.eatup.commercial.domain.provider.ProviderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProviderRepository extends JpaRepository<ProviderDomain, UUID> {

    boolean existsByEmail(String email);

    Optional<ProviderDomain> findByEmail(String email);

    boolean existsByDocumentNumber(String documentNumber);

    Optional<ProviderDomain> findByDocumentNumber(String documentNumber);

    boolean existsByDocumentNumberAndIdNot(String documentNumber, UUID id);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, UUID id);

    List<ProviderDomain> findByStatus(ProviderStatus status);
}