package com.bahadircolak.croesus.controller;

import com.bahadircolak.croesus.model.Portfolio;
import com.bahadircolak.croesus.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Portfolio>> getUserPortfolio() {
        return ResponseEntity.ok(portfolioService.getUserPortfolio());
    }
    
    @GetMapping("/summary")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, BigDecimal>> getPortfolioSummary() {
        return ResponseEntity.ok(Map.of(
                "totalValue", portfolioService.getPortfolioTotalValue(),
                "totalProfitLoss", portfolioService.getPortfolioTotalProfitLoss()
        ));
    }
} 