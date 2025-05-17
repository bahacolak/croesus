package com.bahadircolak.croesus.service;

import com.bahadircolak.croesus.model.Asset;
import com.bahadircolak.croesus.model.Asset.AssetType;
import com.bahadircolak.croesus.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public List<Asset> getAssetsByType(AssetType type) {
        return assetRepository.findByType(type);
    }

    public Optional<Asset> getAssetBySymbol(String symbol) {
        return assetRepository.findBySymbol(symbol);
    }
} 