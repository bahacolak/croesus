package com.bahadircolak.market.controller;

import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.dto.response.MessageResponse;
import com.bahadircolak.market.service.IMarketService;
import com.bahadircolak.market.validation.MarketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final IMarketService marketService;
    private final MarketValidator validator;

    @GetMapping
    public ResponseEntity<List<AssetResponse>> getAllAssets() {
        List<AssetResponse> assets = marketService.getAllAssets();
        return ResponseEntity.ok(assets);
    }

    @GetMapping("/overview")
    public ResponseEntity<List<AssetResponse>> getMarketOverview() {
        List<AssetResponse> overview = marketService.getMarketOverview();
        return ResponseEntity.ok(overview);
    }

    @GetMapping("/gainers")
    public ResponseEntity<List<AssetResponse>> getTopGainers() {
        List<AssetResponse> gainers = marketService.getTopGainers();
        return ResponseEntity.ok(gainers);
    }

    @GetMapping("/losers")
    public ResponseEntity<List<AssetResponse>> getTopLosers() {
        List<AssetResponse> losers = marketService.getTopLosers();
        return ResponseEntity.ok(losers);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AssetResponse>> searchAssets(@RequestParam("q") String query) {
        validator.validateSearchQuery(query);
        List<AssetResponse> assets = marketService.searchAssets(query);
        return ResponseEntity.ok(assets);
    }

    @PostMapping("/refresh")
    public ResponseEntity<MessageResponse> refreshMarketData() {
        List<AssetResponse> updatedAssets = marketService.refreshMarketData();
        String message = buildRefreshSuccessMessage(updatedAssets.size());
        return ResponseEntity.ok(new MessageResponse(message, true));
    }

    private String buildRefreshSuccessMessage(int count) {
        return String.format("Successfully updated %d assets", count);
    }
} 