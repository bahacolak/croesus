package com.bahadircolak.market.exception;

public class MarketDataException extends MarketException {
    
    public MarketDataException(String message) {
        super(message, "MARKET_DATA_ERROR");
    }
    
    public MarketDataException(String message, Throwable cause) {
        super(message, "MARKET_DATA_ERROR", cause);
    }
} 