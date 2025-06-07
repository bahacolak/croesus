package com.bahadircolak.portfolio.service;

import com.bahadircolak.portfolio.model.Asset;
import com.bahadircolak.portfolio.model.Asset.AssetType;
import com.bahadircolak.portfolio.repository.AssetRepository;
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

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset saveAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public boolean existsBySymbol(String symbol) {
        return assetRepository.existsBySymbol(symbol);
    }
} 