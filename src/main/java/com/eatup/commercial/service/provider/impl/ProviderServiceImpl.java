package com.eatup.commercial.service.provider.impl;

import com.eatup.commercial.domain.provider.ProviderDomain;
import com.eatup.commercial.domain.provider.ProviderStatus;
import com.eatup.commercial.dto.provider.ProviderDTO;
import com.eatup.commercial.repository.provider.ProviderRepository;
import com.eatup.commercial.service.provider.ProviderService;
import com.eatup.commercial.utils.provider.exceptions.ProviderBusinessException;
import com.eatup.commercial.utils.provider.exceptions.ProviderNotFoundException;
import com.eatup.commercial.utils.provider.exceptions.ProviderValidationException;
import com.eatup.commercial.utils.provider.mapper.ProviderMapper;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ProviderServiceImpl implements ProviderService {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    private static final Pattern DIGITS_PATTERN = Pattern.compile("^\\d+$");

    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;
    private final EntityManager entityManager;

    public ProviderServiceImpl(
            ProviderRepository providerRepository,
            ProviderMapper providerMapper,
            EntityManager entityManager
    ) {
        this.providerRepository = providerRepository;
        this.providerMapper = providerMapper;
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ProviderDTO createProvider(ProviderDTO request) {
        validateProviderPayload(request);

        ProviderDomain existing = findExistingProviderFromCreateEvent(request);
        if (existing != null) {
            return providerMapper.toDto(existing);
        }

        LocalDateTime now = LocalDateTime.now();

        ProviderDomain domain = providerMapper.toDomain(request);
        domain.setId(UUID.randomUUID());
        domain.setBusinessName(normalizeText(request.getBusinessName()));
        domain.setDocumentTypeId(request.getDocumentTypeId());
        domain.setDocumentNumber(normalizeText(request.getDocumentNumber()));
        domain.setTaxRegimeId(request.getTaxRegimeId());
        domain.setResponsibleFirstName(normalizeText(request.getResponsibleFirstName()));
        domain.setResponsibleLastName(normalizeText(request.getResponsibleLastName()));
        domain.setPhone(normalizeText(request.getPhone()));
        domain.setEmail(normalizeEmail(request.getEmail()));
        domain.setDepartmentId(request.getDepartmentId());
        domain.setCityId(request.getCityId());
        domain.setAddress(normalizeText(request.getAddress()));
        domain.setBranchId(request.getBranchId());
        domain.setStatus(ProviderStatus.ACTIVE);
        domain.setCreatedDate(now);
        domain.setModifiedDate(now);

        entityManager.persist(domain);

        return providerMapper.toDto(domain);
    }

    @Override
    public ProviderDTO getProviderById(UUID providerId) {
        validateId(providerId, "providerId");
        return providerMapper.toDto(findProviderById(providerId));
    }

    @Override
    public List<ProviderDTO> getProviders(String status) {
        List<ProviderDomain> providers;

        if (status == null || status.isBlank()) {
            providers = providerRepository.findAll();
        } else {
            ProviderStatus parsedStatus = parseStatus(status);
            providers = providerRepository.findByStatus(parsedStatus);
        }

        return providers.stream()
                .sorted(Comparator.comparing(ProviderDomain::getCreatedDate))
                .map(providerMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public ProviderDTO updateProvider(UUID providerId, ProviderDTO request) {
        validateId(providerId, "providerId");
        validateProviderPayload(request);

        ProviderDomain existing = findProviderById(providerId);

        validateImmutableEmail(existing.getEmail(), normalizeEmail(request.getEmail()));
        validateDuplicateDocumentOnUpdate(request.getDocumentNumber(), providerId);
        validateDuplicatePhoneOnUpdate(request.getPhone(), providerId);

        existing.setBusinessName(normalizeText(request.getBusinessName()));
        existing.setDocumentTypeId(request.getDocumentTypeId());
        existing.setDocumentNumber(normalizeText(request.getDocumentNumber()));
        existing.setTaxRegimeId(request.getTaxRegimeId());
        existing.setResponsibleFirstName(normalizeText(request.getResponsibleFirstName()));
        existing.setResponsibleLastName(normalizeText(request.getResponsibleLastName()));
        existing.setPhone(normalizeText(request.getPhone()));
        existing.setDepartmentId(request.getDepartmentId());
        existing.setCityId(request.getCityId());
        existing.setAddress(normalizeText(request.getAddress()));
        existing.setBranchId(request.getBranchId());
        existing.setModifiedDate(LocalDateTime.now());

        providerRepository.save(existing);

        return providerMapper.toDto(existing);
    }

    @Override
    @Transactional
    public ProviderDTO updateProvider(String providerId, ProviderDTO request) {
        return updateProvider(parseUUID(providerId, "providerId"), request);
    }

    @Override
    @Transactional
    public ProviderDTO updateStatus(UUID providerId, String status) {
        validateId(providerId, "providerId");

        ProviderStatus newStatus = parseRequiredStatus(status);
        ProviderDomain existing = findProviderById(providerId);

        existing.setStatus(newStatus);
        existing.setModifiedDate(LocalDateTime.now());

        providerRepository.save(existing);

        return providerMapper.toDto(existing);
    }

    @Override
    @Transactional
    public ProviderDTO updateStatus(String providerId, String status) {
        return updateStatus(parseUUID(providerId, "providerId"), status);
    }

    // ── private helpers ────────────────────────────────────────────────────────

    private ProviderDomain findExistingProviderFromCreateEvent(ProviderDTO request) {
        if (request.getId() != null) {
            ProviderDomain byId = providerRepository.findById(request.getId()).orElse(null);
            if (byId != null) return byId;
        }

        String email = normalizeEmail(request.getEmail());
        ProviderDomain byEmail = providerRepository.findByEmail(email).orElse(null);
        if (byEmail != null) return byEmail;

        String documentNumber = normalizeText(request.getDocumentNumber());
        return providerRepository.findByDocumentNumber(documentNumber).orElse(null);
    }

    private ProviderDomain findProviderById(UUID providerId) {
        return providerRepository.findById(providerId)
                .orElseThrow(() -> new ProviderNotFoundException("Provider not found with id: " + providerId));
    }

    private void validateProviderPayload(ProviderDTO request) {
        if (request == null) {
            throw new ProviderValidationException("Request body is required");
        }
        validateRequiredText(request.getBusinessName(), "businessName");
        validateRequiredObject(request.getDocumentTypeId(), "documentTypeId");
        validateRequiredText(request.getDocumentNumber(), "documentNumber");
        validateRequiredObject(request.getTaxRegimeId(), "taxRegimeId");
        validateRequiredText(request.getResponsibleFirstName(), "responsibleFirstName");
        validateRequiredText(request.getResponsibleLastName(), "responsibleLastName");
        validateRequiredText(request.getPhone(), "phone");
        validateRequiredText(request.getEmail(), "email");
        validateRequiredObject(request.getDepartmentId(), "departmentId");
        validateRequiredObject(request.getCityId(), "cityId");
        validateRequiredText(request.getAddress(), "address");
        validateRequiredObject(request.getBranchId(), "branchId");

        validateEmail(request.getEmail());
        validatePhone(request.getPhone());
        validateDocumentNumber(request.getDocumentNumber());
        validateName(request.getResponsibleFirstName(), "responsibleFirstName");
        validateName(request.getResponsibleLastName(), "responsibleLastName");
    }

    private void validateId(UUID value, String fieldName) {
        if (value == null) {
            throw new ProviderValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private UUID parseUUID(String value, String fieldName) {
        validateRequiredText(value, fieldName);
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ProviderValidationException("Field '" + fieldName + "' has invalid UUID format");
        }
    }

    private ProviderStatus parseStatus(String status) {
        try {
            return ProviderStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ProviderValidationException("Invalid provider status value");
        }
    }

    private ProviderStatus parseRequiredStatus(String status) {
        validateRequiredText(status, "status");
        return parseStatus(status);
    }

    private void validateRequiredText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ProviderValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateRequiredObject(Object value, String fieldName) {
        if (value == null) {
            throw new ProviderValidationException("Field '" + fieldName + "' is required and cannot be empty");
        }
    }

    private void validateDocumentNumber(String documentNumber) {
        String normalized = normalizeText(documentNumber);
        if (!DIGITS_PATTERN.matcher(normalized).matches()) {
            throw new ProviderValidationException("Document number must contain only digits");
        }
        if (normalized.length() < 6 || normalized.length() > 20) {
            throw new ProviderValidationException("Document number must be between 6 and 20 digits");
        }
    }

    private void validateName(String value, String fieldName) {
        String normalized = normalizeText(value);
        if (!NAME_PATTERN.matcher(normalized).matches()) {
            throw new ProviderValidationException("Field '" + fieldName + "' must contain only letters");
        }
        if (normalized.length() > 100) {
            throw new ProviderValidationException("Field '" + fieldName + "' must not exceed 100 characters");
        }
    }

    private void validateEmail(String email) {
        String normalized = normalizeEmail(email);
        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new ProviderValidationException(
                    "Invalid email format: '" + email + "'. Expected format: example@domain.com"
            );
        }
    }

    private void validatePhone(String phone) {
        String normalized = normalizeText(phone);
        if (!DIGITS_PATTERN.matcher(normalized).matches()) {
            throw new ProviderValidationException("Phone number must contain only digits");
        }
        if (normalized.length() != 10) {
            throw new ProviderValidationException("Phone number must contain exactly 10 digits");
        }
    }

    private void validateDuplicateDocumentOnUpdate(String documentNumber, UUID currentProviderId) {
        String normalized = normalizeText(documentNumber);
        if (providerRepository.existsByDocumentNumberAndIdNot(normalized, currentProviderId)) {
            throw new ProviderBusinessException(
                    "A provider with document number '" + normalized + "' already exists"
            );
        }
    }

    private void validateDuplicatePhoneOnUpdate(String phone, UUID currentProviderId) {
        String normalized = normalizeText(phone);
        if (providerRepository.existsByPhoneAndIdNot(normalized, currentProviderId)) {
            throw new ProviderBusinessException(
                    "A provider with phone '" + normalized + "' already exists"
            );
        }
    }

    private void validateImmutableEmail(String currentEmail, String requestedEmail) {
        if (!currentEmail.equals(requestedEmail)) {
            throw new ProviderBusinessException(
                    "Email address cannot be modified once the provider has been created"
            );
        }
    }

    private String normalizeText(String value) {
        return value.trim();
    }

    private String normalizeEmail(String value) {
        return value.trim().toLowerCase();
    }
}