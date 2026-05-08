package com.eatup.commercial.service.seller;

import com.eatup.commercial.dto.seller.SellerDTO;
import com.eatup.commercial.dto.seller.SellerPatchDTO;

import java.util.List;
import java.util.UUID;

public interface SellerService {

    SellerDTO createSeller(SellerDTO request);

    SellerDTO getSellerById(UUID sellerId);

    List<SellerDTO> getSellers(String status);

    SellerDTO updateSeller(UUID sellerId, SellerDTO request);

    SellerDTO updateSeller(String sellerId, SellerDTO request);

    SellerDTO updateStatus(UUID sellerId, String status);

    SellerDTO updateSellerStatus(String sellerId, String status);

    SellerDTO patchSeller(UUID sellerId, SellerPatchDTO request);

    SellerDTO patchSeller(String sellerId, SellerPatchDTO request);
}