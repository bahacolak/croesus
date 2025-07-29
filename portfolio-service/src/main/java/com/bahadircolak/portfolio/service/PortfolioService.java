package com.bahadircolak.portfolio.service;

import com.bahadircolak.portfolio.dto.request.UpdatePortfolioRequest;
import com.bahadircolak.portfolio.model.Portfolio;

import java.math.BigDecimal;
import java.util.List;

public interface PortfolioService {
    
    List<Portfolio> getUserPortfolio();
    
    BigDecimal getPortfolioTotalValue();
    
    BigDecimal getPortfolioTotalProfitLoss();
    
    Portfolio getPortfolioByUserAndAsset(Long userId, Long assetId);
    
    Portfolio getPortfolioByUserAndSymbol(Long userId, String symbol);
    
    Long getAssetIdBySymbol(String symbol);
    
    Portfolio updatePortfolio(Long userId, Long assetId, UpdatePortfolioRequest request);
} 