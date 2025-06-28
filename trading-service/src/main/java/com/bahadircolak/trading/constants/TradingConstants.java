package com.bahadircolak.trading.constants;

import java.math.BigDecimal;

public final class TradingConstants {
    
    // Transaction Types
    public static final String BUY_OPERATION = "BUY";
    public static final String SELL_OPERATION = "SELL";
    
    // Trading Limits
    public static final BigDecimal MINIMUM_TRADE_AMOUNT = new BigDecimal("0.001");
    public static final BigDecimal MAXIMUM_TRADE_AMOUNT = new BigDecimal("1000000");
    
    // Default Descriptions
    public static final String DEFAULT_BUY_DESCRIPTION = "%s purchase";
    public static final String DEFAULT_SELL_DESCRIPTION = "%s sale";
    
    // Validation Messages
    public static final String INVALID_QUANTITY_MESSAGE = "Quantity must be greater than 0";
    public static final String INVALID_SYMBOL_MESSAGE = "Symbol cannot be null or empty";
    
    private TradingConstants() {
        // Utility class - prevent instantiation
    }
} 