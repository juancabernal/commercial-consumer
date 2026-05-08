package com.eatup.commercial.service.seller.impl;

import com.eatup.commercial.domain.seller.SellerDomain;
import com.eatup.commercial.domain.seller.SellerStatus;
import com.eatup.commercial.dto.seller.SellerDTO;
import com.eatup.commercial.dto.seller.SellerPatchDTO;
import com.eatup.commercial.repository.seller.SellerRepository;
import com.eatup.commercial.service.seller.SellerService;
import com.eatup.commercial.utils.seller.exceptions.SellerBusinessException;
import com.eatup.commercial.utils.seller.exceptions.SellerNotFoundException;
import com.eatup.commercial.utils.seller.exceptions.SellerValidationException;
import com.eatup.commercial.utils.seller.mapper.SellerMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class SellerServiceImpl implements SellerService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");

    private static final double MAX_COMMISSION = 30.0;
    private static final double MIN_COMMISSION = 0.0;

    private static final String FIELD_FIRST_NAME = "firstName";
    private static final String FIELD_LAST_NAME = "lastName";

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;
    private final EntityManager entityManager;

    public SellerServiceImpl(
            SellerRepository sellerRepository,
            SellerMapper sellerMapper,
            EntityManager entityManager
    ) {
        this.sellerRepository = sellerRepository;
        this.sellerMapper = sellerMapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public SellerDTO createSeller(SellerDTO request) {
        validateSellerPayload(request);

        SellerDomain existing = findExistingSellerFromCreateEvent(request);
        if (existing != null) {
            return sellerMapper.toDto(existing);
        }

        LocalDateTime now = LocalDateTime.now();

        SellerDomain sellerDomain = sellerMapper.toDomain(request);

        sellerDomain.setDocumentTypeId(request.getDocumentTypeId());
        sellerDomain.setLocationId(request.getLocationId());
        sellerDomain.setIdentificationNumber(normalizeText(request.getIdentificationNumber()));
        sellerDomain.setFirstName(normalizeText(request.getFirstName()));
        sellerDomain.setLastName(normalizeText(request.getLastName()));
        sellerDomain.setPhone(normalizeText(request.getPhone()));
        sellerDomain.setEmail(normalizeEmail(request.getEmail()));
        sellerDomain.setCommissionPercentage(request.getCommissionPercentage());
        sellerDomain.setStatus(SellerStatus.ACTIVE);
        sellerDomain.setCreatedDate(now);
        sellerDomain.setModifiedDate(now);

        entityManager.persist(sellerDomain);

        return sellerMapper.toDto(sellerDomain);
    }

    @Override
    public SellerDTO getSellerById(UUID sellerId) {
        validateId(sellerId, "sellerId");
        return sellerMapper.toDto(findSellerById(sellerId));
    }

    @Override
    public List<SellerDTO> getSellers(String status) {
        List<SellerDomain> sellers;

        if (status == null || status.isBlank()) {
            sellers = sellerRepository.findAll();
        } else {
            SellerStatus parsedStatus = parseStatus(status);
            sellers = sellerRepository.findByStatus(parsedStatus);
        }

        return sellers.stream()
                .sorted(Comparator.comparing(SellerDomain::getCreatedDate))
                .map(sellerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public SellerDTO updateSeller(UUID sellerId, SellerDTO request) {
        validateId(sellerId, "sellerId");
        validateSellerPayload(request);

        SellerDomain existing = findSellerById(sellerId);

        validateImmutableEmail(existing.getEmail(), normalizeEmail(request.getEmail()));
        validateDuplicateIdentificationOnUpdate(request.getIdentificationNumber(), sellerId);
        validateDuplicatePhoneOnUpdate(request.getPhone(), sellerId);

        existing.setDocumentTypeId(request.getDocumentTypeId());
        existing.setLocationId(request.getLocationId());
        existing.setIdentificationNumber(normalizeText(request.getIdentificationNumber()));
        existing.setFirstName(normalizeText(request.getFirstName()));
        existing.setLastName(normalizeText(request.getLastName()));
        existing.setPhone(normalizeText(request.getPhone()));
        existing.setCommissionPercentage(request.getCommissionPercentage());
        existing.setEmail(normalizeEmail(request.getEmail()));
        existing.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(existing);

        return sellerMapper.toDto(existing);
    }

    @Override
    @Transactional
    public SellerDTO updateSeller(String sellerId, SellerDTO request) {
        return updateSeller(parseUUID(sellerId, "sellerId"), request);
    }

    @Override
    @Transactional
    public SellerDTO updateStatus(UUID sellerId, String status) {
        validateId(sellerId, "sellerId");

        SellerStatus newStatus = parseRequiredStatus(status);
        SellerDomain existing = findSellerById(sellerId);

        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(existing);

        return sellerMapper.toDto(existing);
    }

    @Override
    @Transactional
    public SellerDTO updateSellerStatus(String sellerId, String status) {
        return updateStatus(parseUUID(sellerId, "sellerId"), status);
    }

    @Override
    @Transactional
    public SellerDTO patchSeller(UUID sellerId, SellerPatchDTO request) {
        validateId(sellerId, "sellerId");

        if (request == null) {
            throw new SellerValidationException("Request body is required");
        }

        SellerDomain existing = findSellerById(sellerId);

        if (request.getFirstName() != null) {
            validateRequiredText(request.getFirstName(), FIELD_FIRST_NAME);
            validateName(request.getFirstName(), FIELD_FIRST_NAME);
            existing.setFirstName(normalizeText(request.getFirstName()));
        }

        if (request.getLastName() != null) {
            validateRequiredText(request.getLastName(), FIELD_LAST_NAME);
            validateName(request.getLastName(), FIELD_LAST_NAME);
            existing.setLastName(normalizeText(request.getLastName()));
        }

        if (request.getPhone() != null) {
            validateRequiredText(request.getPhone(), "phone");
            validatePhone(request.getPhone());
            validateDuplicatePhoneOnUpdate(request.getPhone(), sellerId);
            existing.setPhone(normalizeText(request.getPhone()));
        }

        if (request.getCommissionPercentage() != null) {
            validateCommissionPercentage(request.getCommissionPercentage());
            existing.setCommissionPercentage(request.getCommissionPercentage());
        }

        if (request.getIdentificationNumber() != null) {
            validateRequiredText(request.getIdentificationNumber(), "identificationNumber");
            validateIdentificationNumber(request.getIdentificationNumber());
            validateDuplicateIdentificationOnUpdate(request.getIdentificationNumber(), sellerId);
            existing.setIdentificationNumber(normalizeText(request.getIdentificationNumber()));
        }

        if (request.getLocationId() != null) {
            existing.setLocationId(request.getLocationId());
        }

        if (request.getDocumentTypeId() != null) {
            existing.setDocumentTypeId(request.getDocumentTypeId());
        }

        existing.setModifiedDate(LocalDateTime.now());

        sellerRepository.save(existing);

        return sellerMapper.toDto(existing);
    }

    @Override
    @Transactional
    public SellerDTO patchSeller(String sellerId, SellerPatchDTO request) {
        return patchSeller(parseUUID(sellerId, "sellerId"), request);
    }

    private SellerDomain findExistingSellerFromCreateEvent(SellerDTO request) {
        if (request.getId() != null) {
            SellerDomain existingById = sellerRepository.findById(request.getId()).orElse(null);
            if (existingById != null) {
                return existingById;
            }
        }

        String email = normalizeEmail(request.getEmail());
        SellerDomain existingByEmail = sellerRepository.findByEmail(email).orElse(null);
        if (existingByEmail != null) {
            return existingByEmail;
        }

        String identificationNumber = normalizeText(request.getIdentificationNumber());
        SellerDomain existingByIdentification = sellerRepository
                .findByIdentificationNumber(identificationNumber)
                .orElse(null);
        if (existingByIdentification != null) {
            return existingByIdentification;
        }

        String phone = normalizeText(request.getPhone());
        return sellerRepository.findByPhone(phone).orElse(null);
    }

    private SellerDomain findSellerById(UUID sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new SellerNotFoundException("Seller not found with id: " + sellerId));
    }

    private void validateSellerPayload(SellerDTO request) {
        if (request == null) {
            throw new SellerValidationException("Request body is required");
        }

        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredObject(request.getLocationId(), "locationId");
        validateRequiredText(request.getIdentificationNumber(), "identificationNumber");
        validateRequiredText(request.getFirstName(), FIELD_FIRST_NAME);
        validateRequiredText(request.getLastName(), FIELD_LAST_NAME);
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getCommissionPercentage(), "commissionPercentage");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateCommissionPercentage(request.getCommissionPercentage());
        validateIdentificationNumber(request.getIdentificationNumber());
        validateName(request.getFirstName(), FIELD_FIRST_NAME);
        validateName(request.getLastName(), FIELD_LAST_NAME);
    }

    private void validateId(UUID value, String fieldName) {
        if (value == null) {
            throw new SellerValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private UUID parseUUID(String value, String fieldName) {
        validateRequiredText(value, fieldName);

        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new SellerValidationException("Field '" + fieldName + "' has invalid UUID format");
        }
    }

    private SellerStatus parseStatus(String status) {
        try {
            return SellerStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new SellerValidationException("Invalid seller status value");
        }
    }

    private SellerStatus parseRequiredStatus(String status) {
        validateRequiredText(status, "status");
        return parseStatus(status);
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new SellerValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new SellerValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateIdentificationNumber(String number) {
        String normalized = normalizeText(number);

        if (!DIGITS_PATTERN.matcher(normalized).matches()) {
            throw new SellerValidationException("Identification number must contain only digits");
        }

        if (normalized.length() < 6 || normalized.length() > 20) {
            throw new SellerValidationException("Identification number must be between 6 and 20 digits");
        }
    }

    private void validateName(String value, String fieldName) {
        String normalized = normalizeText(value);

        if (!NAME_PATTERN.matcher(normalized).matches()) {
            throw new SellerValidationException("Field '" + fieldName + "' must contain only letters");
        }

        if (normalized.length() > 100) {
            throw new SellerValidationException("Field '" + fieldName + "' must not exceed 100 characters");
        }
    }

    private void validateEmail(String email) {
        String normalized = normalizeEmail(email);

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new SellerValidationException(
                    "Invalid email format: '" + email + "'. Expected format: example@domain.com"
            );
        }
    }

    private void validatePhone(String phone) {
        String normalized = normalizeText(phone);

        if (!DIGITS_PATTERN.matcher(normalized).matches()) {
            throw new SellerValidationException("Phone number must contain only digits");
        }

        if (normalized.length() != 10) {
            throw new SellerValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateCommissionPercentage(Double commission) {
        if (commission == null) {
            throw new SellerValidationException("Field 'commissionPercentage' is required and cannot be empty");
        }

        if (commission < MIN_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot be negative");
        }

        if (commission > MAX_COMMISSION) {
            throw new SellerValidationException("Commission percentage cannot exceed 30%");
        }

        BigDecimal value = BigDecimal.valueOf(commission).stripTrailingZeros();

        if (value.scale() > 2) {
            throw new SellerValidationException("Commission percentage must have at most 2 decimal places");
        }
    }

    private void validateDuplicateIdentificationOnUpdate(String identificationNumber, UUID currentSellerId) {
        String normalized = normalizeText(identificationNumber);

        if (sellerRepository.existsByIdentificationNumberAndIdNot(normalized, currentSellerId)) {
            throw new SellerBusinessException(
                    "A seller with identification number '" + normalized + "' already exists"
            );
        }
    }

    private void validateDuplicatePhoneOnUpdate(String phone, UUID currentSellerId) {
        String normalized = normalizeText(phone);

        if (sellerRepository.existsByPhoneAndIdNot(normalized, currentSellerId)) {
            throw new SellerBusinessException("A seller with phone '" + normalized + "' already exists");
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new SellerBusinessException("Email address cannot be modified once the seller has been created");
        }
    }

    private String normalizeText(String value) {
        return value.trim();
    }

    private String normalizeEmail(String value) {
        return value.trim().toLowerCase();
    }
}