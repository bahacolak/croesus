package com.bahadircolak.portfolio.service;

import com.bahadircolak.portfolio.constants.PortfolioConstants;
import com.bahadircolak.portfolio.dto.request.UpdatePortfolioRequest;
import com.bahadircolak.portfolio.exception.InsufficientQuantityException;
import com.bahadircolak.portfolio.exception.PortfolioException;
import com.bahadircolak.portfolio.model.Asset;
import com.bahadircolak.portfolio.model.Portfolio;
import com.bahadircolak.portfolio.repository.PortfolioRepository;
import com.bahadircolak.portfolio.validation.PortfolioValidator;
import com.bahadircolak.common.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService implements IPortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserClient userClient;
    private final AssetService assetService;
    private final PortfolioValidator portfolioValidator;

    @Override
    public List<Portfolio> getUserPortfolio() {
        Long userId = getCurrentUserId();
        return portfolioRepository.findByUserId(userId);
    }

    @Override
    public BigDecimal getPortfolioTotalValue() {
        Long userId = getCurrentUserId();
        BigDecimal totalValue = portfolioRepository.getTotalValueByUserId(userId);
        return totalValue != null ? totalValue : PortfolioConstants.ZERO;
    }

    @Override
    public BigDecimal getPortfolioTotalProfitLoss() {
        Long userId = getCurrentUserId();
        BigDecimal totalProfitLoss = portfolioRepository.getTotalProfitLossByUserId(userId);
        return totalProfitLoss != null ? totalProfitLoss : PortfolioConstants.ZERO;
    }

    @Override
    public Portfolio getPortfolioByUserAndAsset(Long userId, Long assetId) {
        return portfolioRepository.findByUserIdAndAssetId(userId, assetId).orElse(null);
    }

    @Override
    public Portfolio getPortfolioByUserAndSymbol(Long userId, String symbol) {
        return portfolioRepository.findByUserIdAndAssetSymbol(userId, symbol).orElse(null);
    }

    public Long getAssetIdBySymbol(String symbol) {
        return assetService.getAssetBySymbol(symbol)
                .map(Asset::getId)
                .orElse(null);
    }

    @Override
    @Transactional
    public Portfolio updatePortfolio(Long userId, Long assetId, UpdatePortfolioRequest request) {
        try {
            System.out.println("DEBUG: Portfolio update request received - userId: " + userId + ", assetId: " + assetId);
            System.out.println("DEBUG: Request details - " + request);
            
            // Temporary: Skip validation to debug issue
            // portfolioValidator.validateUpdateRequest(request);
            
            System.out.println("DEBUG: Getting or creating asset...");
            Asset asset = getOrCreateAsset(assetId, request);
            System.out.println("DEBUG: Asset created/found: " + asset.getSymbol());
            
            System.out.println("DEBUG: Getting existing portfolio...");
            Portfolio portfolio = getExistingPortfolio(userId, asset.getId());
            System.out.println("DEBUG: Portfolio found: " + (portfolio != null));
            
            if (PortfolioConstants.BUY_ACTION.equals(request.getAction())) {
                System.out.println("DEBUG: Processing BUY operation...");
                portfolio = processBuyOperation(portfolio, asset, userId, request);
            } else if (PortfolioConstants.SELL_ACTION.equals(request.getAction())) {
                System.out.println("DEBUG: Processing SELL operation...");
                portfolio = processSellOperation(portfolio, request);
                if (portfolio == null) return null; // Portfolio deleted
            }
            
            System.out.println("DEBUG: Updating portfolio metrics...");
            updatePortfolioMetrics(portfolio, asset);
            
            System.out.println("DEBUG: Saving portfolio...");
            Portfolio saved = portfolioRepository.save(portfolio);
            System.out.println("DEBUG: Portfolio saved successfully");
            return saved;
            
        } catch (Exception e) {
            System.err.println("ERROR in updatePortfolio: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userClient.getUserIdByUsername(userDetails.getUsername());
    }
    
    private Asset getOrCreateAsset(Long assetId, UpdatePortfolioRequest request) {
        Asset asset = assetService.getAssetById(assetId).orElse(null);
        if (asset == null) {
            asset = assetService.getAssetBySymbol(request.getAssetSymbol()).orElse(null);
            if (asset == null) {
                asset = createNewAsset(request);
            } else {
                asset.setCurrentPrice(request.getPrice());
                asset = assetService.saveAsset(asset);
            }
        } else {
            asset.setCurrentPrice(request.getPrice());
            asset = assetService.saveAsset(asset);
        }
        return asset;
    }
    
    private Asset createNewAsset(UpdatePortfolioRequest request) {
        Asset asset = new Asset();
        asset.setSymbol(request.getAssetSymbol());
        asset.setName(request.getAssetName());
        asset.setType(Asset.AssetType.CRYPTO);
        asset.setCurrentPrice(request.getPrice());
        asset.setPriceChange24h(PortfolioConstants.ZERO);
        asset.setPriceChangePercent24h(PortfolioConstants.ZERO);
        return assetService.saveAsset(asset);
    }
    
    private Portfolio getExistingPortfolio(Long userId, Long assetId) {
        return portfolioRepository.findByUserIdAndAssetId(userId, assetId).orElse(null);
    }
    
    private Portfolio processBuyOperation(Portfolio portfolio, Asset asset, Long userId, UpdatePortfolioRequest request) {
        if (portfolio == null) {
            return createNewPortfolio(userId, asset, request);
        } else {
            return updateExistingPortfolioForBuy(portfolio, request);
        }
    }
    
    private Portfolio createNewPortfolio(Long userId, Asset asset, UpdatePortfolioRequest request) {
        Portfolio portfolio = new Portfolio();
        portfolio.setUserId(userId);
        portfolio.setAsset(asset);
        portfolio.setQuantity(request.getQuantity());
        portfolio.setAverageBuyPrice(request.getPrice());
        portfolio.setTotalInvestment(request.getQuantity().multiply(request.getPrice()));
        return portfolio;
    }
    
    private Portfolio updateExistingPortfolioForBuy(Portfolio portfolio, UpdatePortfolioRequest request) {
        BigDecimal newInvestment = request.getQuantity().multiply(request.getPrice());
        BigDecimal totalNewInvestment = portfolio.getTotalInvestment().add(newInvestment);
        BigDecimal newTotalQuantity = portfolio.getQuantity().add(request.getQuantity());
        BigDecimal newAveragePrice = totalNewInvestment.divide(newTotalQuantity, PortfolioConstants.CALCULATION_SCALE, RoundingMode.HALF_UP);

        portfolio.setQuantity(newTotalQuantity);
        portfolio.setAverageBuyPrice(newAveragePrice);
        portfolio.setTotalInvestment(totalNewInvestment);
        return portfolio;
    }
    
    private Portfolio processSellOperation(Portfolio portfolio, UpdatePortfolioRequest request) {
        if (portfolio == null) {
            throw new PortfolioException("Cannot sell asset not in portfolio", "CANNOT_SELL_EMPTY_PORTFOLIO");
        }
        
        if (portfolio.getQuantity().compareTo(request.getQuantity()) < 0) {
            throw new InsufficientQuantityException(portfolio.getQuantity(), request.getQuantity());
        }

        BigDecimal remainingQuantity = portfolio.getQuantity().subtract(request.getQuantity());
        
        if (remainingQuantity.compareTo(PortfolioConstants.ZERO) == 0) {
            portfolioRepository.delete(portfolio);
            return null;
        } else {
            return updatePortfolioForSell(portfolio, request, remainingQuantity);
        }
    }
    
    private Portfolio updatePortfolioForSell(Portfolio portfolio, UpdatePortfolioRequest request, BigDecimal remainingQuantity) {
        BigDecimal proportionSold = request.getQuantity().divide(portfolio.getQuantity(), PortfolioConstants.CALCULATION_SCALE, RoundingMode.HALF_UP);
        BigDecimal investmentReduction = portfolio.getTotalInvestment().multiply(proportionSold);
        
        portfolio.setQuantity(remainingQuantity);
        portfolio.setTotalInvestment(portfolio.getTotalInvestment().subtract(investmentReduction));
        return portfolio;
    }
    
    private void updatePortfolioMetrics(Portfolio portfolio, Asset asset) {
        if (portfolio == null) return;
        
        BigDecimal currentValue = portfolio.getQuantity().multiply(asset.getCurrentPrice());
        BigDecimal profitLoss = currentValue.subtract(portfolio.getTotalInvestment());
        BigDecimal profitLossPercentage = calculateProfitLossPercentage(profitLoss, portfolio.getTotalInvestment());

        portfolio.setCurrentValue(currentValue);
        portfolio.setProfitLoss(profitLoss);
        portfolio.setProfitLossPercentage(profitLossPercentage);
    }
    
    private BigDecimal calculateProfitLossPercentage(BigDecimal profitLoss, BigDecimal totalInvestment) {
        return totalInvestment.compareTo(PortfolioConstants.ZERO) > 0 
            ? profitLoss.divide(totalInvestment, PortfolioConstants.PERCENTAGE_SCALE, RoundingMode.HALF_UP).multiply(PortfolioConstants.HUNDRED)
            : PortfolioConstants.ZERO;
    }
} 