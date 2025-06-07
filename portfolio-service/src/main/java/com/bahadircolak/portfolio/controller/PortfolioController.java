package com.bahadircolak.portfolio.controller;

import com.bahadircolak.portfolio.model.Portfolio;
import com.bahadircolak.portfolio.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<Portfolio>> getUserPortfolio() {
        return ResponseEntity.ok(portfolioService.getUserPortfolio());
    }
    
    @GetMapping("/summary")
    public ResponseEntity<Map<String, BigDecimal>> getPortfolioSummary() {
        return ResponseEntity.ok(Map.of(
                "totalValue", portfolioService.getPortfolioTotalValue(),
                "totalProfitLoss", portfolioService.getPortfolioTotalProfitLoss()
        ));
    }

    @GetMapping("/user/{userId}/asset/{assetId}")
    public ResponseEntity<Portfolio> getPortfolioByUserAndAsset(@PathVariable Long userId, @PathVariable Long assetId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserAndAsset(userId, assetId);
        if (portfolio != null) {
            return ResponseEntity.ok(portfolio);
        }
        return ResponseEntity.notFound().build();
    }
} 