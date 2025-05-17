package com.bahadircolak.croesus.service;

import com.bahadircolak.croesus.model.Portfolio;
import com.bahadircolak.croesus.model.User;
import com.bahadircolak.croesus.repository.PortfolioRepository;
import com.bahadircolak.croesus.repository.UserRepository;
import com.bahadircolak.croesus.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public List<Portfolio> getUserPortfolio() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return portfolioRepository.findByUser(user);
    }
    
    public BigDecimal getPortfolioTotalValue() {
        List<Portfolio> portfolios = getUserPortfolio();
        return portfolios.stream()
                .map(Portfolio::getCurrentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    public BigDecimal getPortfolioTotalProfitLoss() {
        List<Portfolio> portfolios = getUserPortfolio();
        return portfolios.stream()
                .map(Portfolio::getProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
} 