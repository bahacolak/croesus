package com.bahadircolak.market.controller;

import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.dto.response.MessageResponse;
import com.bahadircolak.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
@Slf4j
public class MarketController {

    private final MarketService marketService;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        try {
            List<AssetResponse> assets = marketService.getAllAssets();
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            log.error("Error fetching all assets", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/overview")
    public ResponseEntity<List<AssetResponse>> getMarketOverview() {
        try {
            List<AssetResponse> assets = marketService.getAllAssets();
            // Limit to top 20 for overview
            List<AssetResponse> overview = assets.stream().limit(20).toList();
            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            log.error("Error fetching market overview", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/gainers")
    public ResponseEntity<List<AssetResponse>> getTopGainers() {
        try {
            List<AssetResponse> gainers = marketService.getTopGainers();
            return ResponseEntity.ok(gainers);
        } catch (Exception e) {
            log.error("Error fetching top gainers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/losers")
    public ResponseEntity<List<AssetResponse>> getTopLosers() {
        try {
            List<AssetResponse> losers = marketService.getTopLosers();
            return ResponseEntity.ok(losers);
        } catch (Exception e) {
            log.error("Error fetching top losers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<AssetResponse>> searchAssets(@RequestParam("q") String q) {
        try {
            List<AssetResponse> assets = marketService.searchAssets(q);
            return ResponseEntity.ok(assets);
        } catch (Exception e) {
            log.error("Error searching assets with query: {}", q, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponse> refreshMarketData() {
        try {
            List<AssetResponse> updatedAssets = marketService.refreshMarketData();
            String message = String.format("Successfully updated %d assets", updatedAssets.size());
            return ResponseEntity.ok(new MessageResponse(message));
        } catch (Exception e) {
            log.error("Error refreshing market data", e);
            return ResponseEntity.internalServerError()
                    .body(new MessageResponse("Failed to refresh market data: " + e.getMessage(), false));
        }
    }
} 