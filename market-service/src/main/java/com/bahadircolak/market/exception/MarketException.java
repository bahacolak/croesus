package com.bahadircolak.market.exception;

public class MarketException extends RuntimeException {
    
    private final String errorCode;
    
    public MarketException(String message) {
        super(message);
        this.errorCode = "MARKET_ERROR";
    }
    
    public MarketException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public MarketException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "MARKET_ERROR";
    }
    
    public MarketException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
} 