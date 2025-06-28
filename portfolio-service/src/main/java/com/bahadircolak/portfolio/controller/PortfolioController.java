package com.bahadircolak.portfolio.controller;

import com.bahadircolak.portfolio.dto.request.UpdatePortfolioRequest;
import com.bahadircolak.portfolio.dto.response.PortfolioSummaryResponse;
import com.bahadircolak.portfolio.exception.PortfolioNotFoundException;
import com.bahadircolak.portfolio.model.Portfolio;
import com.bahadircolak.portfolio.service.IPortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class PortfolioController {

    private final IPortfolioService portfolioService;

    @GetMapping
    public ResponseEntity<List<Portfolio>> getUserPortfolio() {
        return ResponseEntity.ok(portfolioService.getUserPortfolio());
    }
    
    @GetMapping("/summary")
    public ResponseEntity<PortfolioSummaryResponse> getPortfolioSummary() {
        PortfolioSummaryResponse summary = new PortfolioSummaryResponse(
                portfolioService.getPortfolioTotalValue(),
                portfolioService.getPortfolioTotalProfitLoss()
        );
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/user/{userId}/asset/{assetId}")
    public ResponseEntity<Portfolio> getPortfolioByUserAndAsset(@PathVariable("userId") Long userId, @PathVariable("assetId") Long assetId) {
        Portfolio portfolio = portfolioService.getPortfolioByUserAndAsset(userId, assetId);
        if (portfolio == null) {
            throw new PortfolioNotFoundException(userId, assetId.toString());
        }
        return ResponseEntity.ok(portfolio);
    }

    @GetMapping("/user/{userId}/symbol/{symbol}")
    public ResponseEntity<Portfolio> getPortfolioByUserAndSymbol(@PathVariable("userId") Long userId, @PathVariable("symbol") String symbol) {
        Portfolio portfolio = portfolioService.getPortfolioByUserAndSymbol(userId, symbol);
        if (portfolio != null) {
            return ResponseEntity.ok(portfolio);  
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/asset/symbol/{symbol}")
    public ResponseEntity<Map<String, Long>> getAssetIdBySymbol(@PathVariable("symbol") String symbol) {
        Long assetId = portfolioService.getAssetIdBySymbol(symbol);
        if (assetId != null) {
            return ResponseEntity.ok(Map.of("id", assetId));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/user/{userId}/asset/{assetId}/update")
    public ResponseEntity<Portfolio> updatePortfolio(@PathVariable("userId") Long userId, 
                                                   @PathVariable("assetId") Long assetId,
                                                   @Valid @RequestBody UpdatePortfolioRequest request) {
        Portfolio portfolio = portfolioService.updatePortfolio(userId, assetId, request);
        return ResponseEntity.ok(portfolio);
    }
} 