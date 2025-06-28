package com.bahadircolak.portfolio.constants;

import java.math.BigDecimal;

public final class PortfolioConstants {
    
    private PortfolioConstants() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    public static final String BUY_ACTION = "BUY";
    public static final String SELL_ACTION = "SELL";
    
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    
    public static final int CALCULATION_SCALE = 8;
    public static final int PERCENTAGE_SCALE = 4;
} 