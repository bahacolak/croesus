package com.bahadircolak.market.constants;

public final class MarketConstants {
    
    public static final int DEFAULT_MARKET_OVERVIEW_LIMIT = 20;
    public static final int DEFAULT_TOP_GAINERS_LIMIT = 10;
    public static final int DEFAULT_TOP_LOSERS_LIMIT = 10;
    public static final int COINGECKO_DEFAULT_PER_PAGE = 100;
    public static final int COINGECKO_DEFAULT_PAGE = 1;
    
    public static final String COINGECKO_VS_CURRENCY = "usd";
    public static final String COINGECKO_ORDER = "market_cap_desc";
    public static final String COINGECKO_SPARKLINE = "false";
    
    public static final long PRICE_UPDATE_INTERVAL = 600000L; // 10 minutes
    public static final String DAILY_REFRESH_CRON = "0 0 0 * * *"; // Midnight
    
    public static final String SYMBOL_UPPER_CASE_REGEX = "^[A-Z]+$";
    public static final int MIN_SEARCH_QUERY_LENGTH = 2;
    public static final int MAX_SEARCH_QUERY_LENGTH = 50;
    public static final int MIN_SYMBOL_LENGTH = 2;
    public static final int MAX_SYMBOL_LENGTH = 10;
    
    private MarketConstants() {
    }
} 