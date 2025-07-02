package com.bahadircolak.market.controller;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.dto.response.AssetResponse;
import com.bahadircolak.market.exception.AssetNotFoundException;
import com.bahadircolak.market.service.IMarketService;
import com.bahadircolak.market.validation.MarketValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final IMarketService marketService;
    private final MarketValidator validator;

    @GetMapping("/{symbol}")
    public ResponseEntity<AssetResponse> getAssetBySymbol(@PathVariable String symbol) {
        validator.validateSymbol(symbol);
        Optional<AssetResponse> asset = marketService.getAssetBySymbol(symbol);
        return asset.map(ResponseEntity::ok)
                .orElseThrow(() -> new AssetNotFoundException(
                    String.format(ErrorMessages.ASSET_NOT_FOUND_BY_SYMBOL, symbol)
                ));
    }

    @GetMapping("/{symbol}/price")
    public ResponseEntity<BigDecimal> getAssetPrice(@PathVariable String symbol) {
        validator.validateSymbol(symbol);
        BigDecimal price = marketService.getAssetPrice(symbol);
        return ResponseEntity.ok(price);
    }

    @GetMapping("/id/{coinId}")
    public ResponseEntity<AssetResponse> getAssetById(@PathVariable String coinId) {
        validator.validateCoinId(coinId);
        Optional<AssetResponse> asset = marketService.getAssetById(coinId);
        return asset.map(ResponseEntity::ok)
                .orElseThrow(() -> new AssetNotFoundException(
                    String.format(ErrorMessages.ASSET_NOT_FOUND_BY_ID, coinId)
                ));
    }
} 