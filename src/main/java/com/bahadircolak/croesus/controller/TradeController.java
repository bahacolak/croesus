package com.bahadircolak.croesus.controller;

import com.bahadircolak.croesus.dto.request.TradeRequest;
import com.bahadircolak.croesus.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/buy")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> buyAsset(@Valid @RequestBody TradeRequest request) {
        return tradeService.buyAsset(request);
    }

    @PostMapping("/sell")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> sellAsset(@Valid @RequestBody TradeRequest request) {
        return tradeService.sellAsset(request);
    }
} 