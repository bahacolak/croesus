package com.bahadircolak.trading.controller;

import com.bahadircolak.trading.dto.request.TradeRequest;
import com.bahadircolak.trading.dto.response.TradeResponse;
import com.bahadircolak.trading.service.TradingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradingController {

    private final TradingService tradingService;

    @PostMapping("/buy")
    public ResponseEntity<TradeResponse> buyAsset(@Valid @RequestBody TradeRequest request) {
        return tradingService.buyAsset(request);
    }

    @PostMapping("/sell")
    public ResponseEntity<TradeResponse> sellAsset(@Valid @RequestBody TradeRequest request) {
        return tradingService.sellAsset(request);
    }
} 