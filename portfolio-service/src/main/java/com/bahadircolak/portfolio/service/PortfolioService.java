package com.bahadircolak.portfolio.service;

import com.bahadircolak.portfolio.dto.request.UpdatePortfolioRequest;
import com.bahadircolak.portfolio.model.Asset;
import com.bahadircolak.portfolio.model.Portfolio;
import com.bahadircolak.portfolio.repository.PortfolioRepository;
import com.bahadircolak.common.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserClient userClient;
    private final AssetService assetService;

    public List<Portfolio> getUserPortfolio() {
        Long userId = getCurrentUserId();
        return portfolioRepository.findByUserId(userId);
    }

    public BigDecimal getPortfolioTotalValue() {
        Long userId = getCurrentUserId();
        BigDecimal totalValue = portfolioRepository.getTotalValueByUserId(userId);
        return totalValue != null ? totalValue : BigDecimal.ZERO;
    }

    public BigDecimal getPortfolioTotalProfitLoss() {
        Long userId = getCurrentUserId();
        BigDecimal totalProfitLoss = portfolioRepository.getTotalProfitLossByUserId(userId);
        return totalProfitLoss != null ? totalProfitLoss : BigDecimal.ZERO;
    }

    public Portfolio getPortfolioByUserAndAsset(Long userId, Long assetId) {
        return portfolioRepository.findByUserIdAndAssetId(userId, assetId).orElse(null);
    }

    public Portfolio getPortfolioByUserAndSymbol(Long userId, String symbol) {
        return portfolioRepository.findByUserIdAndAssetSymbol(userId, symbol).orElse(null);
    }

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);
    }

    @Transactional
    public Portfolio updatePortfolio(Long userId, Long assetId, UpdatePortfolioRequest request) {
        Asset asset = assetService.getAssetById(assetId).orElse(null);
        if (asset == null) {
            asset = assetService.getAssetBySymbol(request.getAssetSymbol()).orElse(null);
            if (asset == null) {
                asset = new Asset();
                asset.setSymbol(request.getAssetSymbol());
                asset.setName(request.getAssetName());
                asset.setType(Asset.AssetType.CRYPTO);
                asset.setCurrentPrice(request.getPrice());
                asset.setPriceChange24h(BigDecimal.ZERO);
                asset.setPriceChangePercent24h(BigDecimal.ZERO);
                asset = assetService.saveAsset(asset);
                log.info("Created new asset: {}", asset.getSymbol());
            } else {
                asset.setCurrentPrice(request.getPrice());
                asset = assetService.saveAsset(asset);
                log.info("Updated existing asset: {}", asset.getSymbol());
            }
        } else {
            asset.setCurrentPrice(request.getPrice());
            asset = assetService.saveAsset(asset);
        }

        Portfolio portfolio = portfolioRepository.findByUserIdAndAssetId(userId, asset.getId()).orElse(null);

        if ("BUY".equals(request.getAction())) {
            if (portfolio == null) {
                portfolio = new Portfolio();
                portfolio.setUserId(userId);
                portfolio.setAsset(asset);
                portfolio.setQuantity(request.getQuantity());
                portfolio.setAverageBuyPrice(request.getPrice());
                portfolio.setTotalInvestment(request.getQuantity().multiply(request.getPrice()));
            } else {
                BigDecimal newInvestment = request.getQuantity().multiply(request.getPrice());
                BigDecimal totalNewInvestment = portfolio.getTotalInvestment().add(newInvestment);
                BigDecimal newTotalQuantity = portfolio.getQuantity().add(request.getQuantity());
                BigDecimal newAveragePrice = totalNewInvestment.divide(newTotalQuantity, 8, RoundingMode.HALF_UP);

                portfolio.setQuantity(newTotalQuantity);
                portfolio.setAverageBuyPrice(newAveragePrice);
                portfolio.setTotalInvestment(totalNewInvestment);
            }
        } else if ("SELL".equals(request.getAction())) {
            if (portfolio == null) {
                throw new RuntimeException("Cannot sell asset not in portfolio");
            }
            
            if (portfolio.getQuantity().compareTo(request.getQuantity()) < 0) {
                throw new RuntimeException("Insufficient quantity to sell");
            }

            BigDecimal soldValue = request.getQuantity().multiply(request.getPrice());
            BigDecimal remainingQuantity = portfolio.getQuantity().subtract(request.getQuantity());
            
            if (remainingQuantity.compareTo(BigDecimal.ZERO) == 0) {
                portfolioRepository.delete(portfolio);
                return portfolio;
            } else {
                BigDecimal proportionSold = request.getQuantity().divide(portfolio.getQuantity(), 8, RoundingMode.HALF_UP);
                BigDecimal investmentReduction = portfolio.getTotalInvestment().multiply(proportionSold);
                
                portfolio.setQuantity(remainingQuantity);
                portfolio.setTotalInvestment(portfolio.getTotalInvestment().subtract(investmentReduction));
            }
        }

        BigDecimal currentValue = portfolio.getQuantity().multiply(asset.getCurrentPrice());
        BigDecimal profitLoss = currentValue.subtract(portfolio.getTotalInvestment());
        BigDecimal profitLossPercentage = portfolio.getTotalInvestment().compareTo(BigDecimal.ZERO) > 0 
            ? profitLoss.divide(portfolio.getTotalInvestment(), 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100))
            : BigDecimal.ZERO;

        portfolio.setCurrentValue(currentValue);
        portfolio.setProfitLoss(profitLoss);
        portfolio.setProfitLossPercentage(profitLossPercentage);

        return portfolioRepository.save(portfolio);
    }

    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userClient.getUserIdByUsername(userDetails.getUsername());
    }
} 