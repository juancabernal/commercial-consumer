package com.eatup.commercial.service.provider;

import com.eatup.commercial.dto.provider.ProviderDTO;

import java.util.List;
import java.util.UUID;

public interface ProviderService {

    ProviderDTO createProvider(ProviderDTO request);

    ProviderDTO getProviderById(UUID providerId);

    List<ProviderDTO> getProviders(String status);

    ProviderDTO updateProvider(UUID providerId, ProviderDTO request);

    ProviderDTO updateProvider(String providerId, ProviderDTO request);

    ProviderDTO updateStatus(UUID providerId, String status);

    ProviderDTO updateStatus(String providerId, String status);
}