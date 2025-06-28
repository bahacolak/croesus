package com.bahadircolak.trading.exception;

import java.math.BigDecimal;

public class InsufficientAssetException extends TradingException {
    
    public InsufficientAssetException(String symbol, BigDecimal current, BigDecimal requested) {
        super(String.format("Insufficient asset %s. Current: %s, Requested to sell: %s", 
              symbol, current, requested), "INSUFFICIENT_ASSET");
    }
    
    public InsufficientAssetException(String message) {
        super(message, "INSUFFICIENT_ASSET");
    }
} 