package com.bahadircolak.market.service;

import com.bahadircolak.market.dto.response.AssetResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IMarketService {
    
    List<AssetResponse> getAllAssets();
    
    List<AssetResponse> getMarketOverview();
    
    Optional<AssetResponse> getAssetBySymbol(String symbol);
    
    Optional<AssetResponse> getAssetById(String coinId);
    
    BigDecimal getAssetPrice(String symbol);
    
    List<AssetResponse> searchAssets(String query);
    
    List<AssetResponse> getTopGainers();
    
    List<AssetResponse> getTopLosers();
    
    List<AssetResponse> refreshMarketData();
} 