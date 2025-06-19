package com.bahadircolak.portfolio.controller;

import com.bahadircolak.portfolio.model.Asset;
import com.bahadircolak.portfolio.model.Asset.AssetType;
import com.bahadircolak.portfolio.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping
    public ResponseEntity<List<Asset>> getAllAssets() {
        return ResponseEntity.ok(assetService.getAllAssets());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Asset>> getAssetsByType(@PathVariable("type") AssetType type) {
        return ResponseEntity.ok(assetService.getAssetsByType(type));
    }

    @GetMapping("/{symbol}")
    public ResponseEntity<Asset> getAssetBySymbol(@PathVariable("symbol") String symbol) {
        return assetService.getAssetBySymbol(symbol)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<Asset> getAssetById(@PathVariable("id") Long id) {
        return assetService.getAssetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
} 