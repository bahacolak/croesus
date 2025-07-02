package com.bahadircolak.market.constants;

public final class ErrorMessages {
    
    public static final String ASSET_NOT_FOUND = "Asset not found";
    public static final String ASSET_NOT_FOUND_BY_SYMBOL = "Asset not found with symbol: %s";
    public static final String ASSET_NOT_FOUND_BY_ID = "Asset not found with ID: %s";
    public static final String PRICE_NOT_AVAILABLE = "Price not available for symbol: %s";
    
    public static final String COINGECKO_API_ERROR = "Failed to fetch data from CoinGecko API";
    public static final String COINGECKO_NO_DATA = "No data received from CoinGecko API";
    public static final String MARKET_DATA_REFRESH_FAILED = "Failed to refresh market data";
    public static final String CRYPTOCURRENCY_UPDATE_FAILED = "Failed to update cryptocurrency data";
    
    public static final String SYMBOL_REQUIRED = "Symbol is required";
    public static final String SYMBOL_INVALID_LENGTH = "Symbol length must be between %d and %d characters";
    public static final String SYMBOL_INVALID_FORMAT = "Symbol must contain only uppercase letters";
    public static final String COIN_ID_REQUIRED = "Coin ID is required";
    public static final String COIN_ID_INVALID = "Invalid coin ID format";
    
    public static final String SEARCH_QUERY_REQUIRED = "Search query is required";
    public static final String SEARCH_QUERY_TOO_SHORT = "Search query must be at least %d characters";
    public static final String SEARCH_QUERY_TOO_LONG = "Search query cannot exceed %d characters";
    
    public static final String GENERAL_ERROR = "An unexpected error occurred";
    
    private ErrorMessages() {
    }
} 