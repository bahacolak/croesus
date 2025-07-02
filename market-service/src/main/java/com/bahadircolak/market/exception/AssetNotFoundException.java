package com.bahadircolak.market.exception;

public class AssetNotFoundException extends MarketException {
    
    public AssetNotFoundException(String message) {
        super(message, "ASSET_NOT_FOUND");
    }
    
    public AssetNotFoundException(String message, Throwable cause) {
        super(message, "ASSET_NOT_FOUND", cause);
    }
} 