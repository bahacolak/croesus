package com.bahadircolak.trading.service;

import com.bahadircolak.trading.dto.request.TradeRequest;
import com.bahadircolak.trading.dto.response.TradeResponse;
import org.springframework.http.ResponseEntity;

public interface ITradingService {
    
    ResponseEntity<TradeResponse> buyAsset(TradeRequest request);
    
    ResponseEntity<TradeResponse> sellAsset(TradeRequest request);
} 