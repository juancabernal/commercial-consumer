package com.eatup.commercial.repository.seller;

import com.eatup.commercial.domain.seller.SellerDomain;
import com.eatup.commercial.domain.seller.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<SellerDomain, UUID> {

    boolean existsByEmail(String email);

    Optional<SellerDomain> findByEmail(String email);

    boolean existsByIdentificationNumber(String identificationNumber);

    Optional<SellerDomain> findByIdentificationNumber(String identificationNumber);

    boolean existsByIdentificationNumberAndIdNot(String identificationNumber, UUID id);

    List<SellerDomain> findByStatus(SellerStatus status);

    boolean existsByPhone(String phone);

    Optional<SellerDomain> findByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, UUID id);
}