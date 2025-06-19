package com.bahadircolak.market.controller;

import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.service.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
@Slf4j
public class AssetController {

    private final MarketService marketService;

    @GetMapping("/{symbol}")
    public ResponseEntity<AssetResponse> getAssetBySymbol(@PathVariable("symbol") String symbol) {
        try {
            Optional<AssetResponse> asset = marketService.getAssetBySymbol(symbol);
            if (asset.isPresent()) {
                return ResponseEntity.ok(asset.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching asset by symbol: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getAssetPrice(@PathVariable("symbol") String symbol) {
        try {
            BigDecimal price = marketService.getAssetPrice(symbol);
            return ResponseEntity.ok(price);
        } catch (RuntimeException e) {
            log.error("Error fetching price for asset: {}", symbol, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error fetching price for asset: {}", symbol, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/id/{coinId}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable("coinId") String coinId) {
        try {
            Optional<AssetResponse> asset = marketService.getAssetById(coinId);
            if (asset.isPresent()) {
                return ResponseEntity.ok(asset.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error fetching asset by ID: {}", coinId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 