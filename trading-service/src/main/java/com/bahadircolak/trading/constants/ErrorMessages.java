package com.bahadircolak.trading.constants;

public final class ErrorMessages {
    
    // Asset related errors
    public static final String ASSET_NOT_FOUND = "Asset not found: %s";
    public static final String ASSET_DATA_INCOMPLETE = "Incomplete asset data for symbol: %s";
    public static final String ASSET_NOT_IN_PORTFOLIO = "Asset %s not found in portfolio";
    
    // Balance related errors
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance. Required: %s, Current: %s";
    public static final String INSUFFICIENT_ASSET = "Insufficient asset. Current: %s, Requested to sell: %s";
    
    // Transaction errors
    public static final String TRANSACTION_FAILED = "Transaction failed: %s";
    public static final String BUY_OPERATION_ERROR = "Error during buy operation: %s";
    public static final String SELL_OPERATION_ERROR = "Error during sell operation: %s";
    
    // Validation errors
    public static final String INVALID_TRADE_REQUEST = "Invalid trade request";
    public static final String INVALID_QUANTITY = "Quantity must be greater than 0";
    public static final String INVALID_SYMBOL = "Symbol cannot be null or empty";
    public static final String INVALID_USER = "User not found or invalid";
    
    private ErrorMessages() {
        // Utility class - prevent instantiation
    }
} 