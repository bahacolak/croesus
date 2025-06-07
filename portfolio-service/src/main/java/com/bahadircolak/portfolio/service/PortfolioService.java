package com.bahadircolak.portfolio.service;

import com.bahadircolak.portfolio.model.Portfolio;
import com.bahadircolak.portfolio.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserService userService;

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

    public Portfolio savePortfolio(Portfolio portfolio) {
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);
    }

    private Long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.getUserIdByUsername(userDetails.getUsername());
    }
} 