package com.bahadircolak.trading.exception;

public class AssetNotFoundException extends TradingException {
    
    public AssetNotFoundException(String symbol) {
        super(String.format("Asset not found: %s", symbol), "ASSET_NOT_FOUND");
    }
    
    public AssetNotFoundException(String symbol, Throwable cause) {
        super(String.format("Asset not found: %s", symbol), "ASSET_NOT_FOUND", cause);
    }
} 