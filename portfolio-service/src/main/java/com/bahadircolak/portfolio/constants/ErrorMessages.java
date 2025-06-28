package com.bahadircolak.portfolio.constants;

public final class ErrorMessages {
    
    private ErrorMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static final String PORTFOLIO_NOT_FOUND = "Portfolio not found for user: %d and asset: %s";
    public static final String ASSET_NOT_FOUND = "Asset not found: %s";
    public static final String INSUFFICIENT_QUANTITY = "Insufficient quantity to sell. Available: %s, Requested: %s";
    public static final String INVALID_ACTION = "Invalid action: %s. Must be BUY or SELL";
    public static final String INVALID_QUANTITY = "Quantity must be greater than zero";
    public static final String INVALID_PRICE = "Price must be greater than zero";
    public static final String INVALID_SYMBOL = "Asset symbol cannot be empty";
    public static final String CANNOT_SELL_EMPTY_PORTFOLIO = "Cannot sell asset not in portfolio";
} 