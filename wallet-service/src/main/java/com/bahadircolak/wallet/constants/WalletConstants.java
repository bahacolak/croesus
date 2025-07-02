package com.bahadircolak.wallet.constants;

import java.math.BigDecimal;

public final class WalletConstants {
    
    public static final String DEFAULT_CURRENCY_CODE = "USD";
    public static final BigDecimal ZERO_BALANCE = BigDecimal.ZERO;
    public static final BigDecimal MIN_TRANSACTION_AMOUNT = new BigDecimal("0.01");
    public static final BigDecimal MAX_TRANSACTION_AMOUNT = new BigDecimal("1000000.00");
    public static final BigDecimal MIN_BALANCE = BigDecimal.ZERO;
    
    public static final String CROSS_ORIGIN_PATTERN = "*";
    public static final long CROSS_ORIGIN_MAX_AGE = 3600;
    
    public static final String DEFAULT_DEPOSIT_DESCRIPTION = "Deposit";
    public static final String DEFAULT_WITHDRAWAL_DESCRIPTION = "Withdrawal";
    public static final String DEFAULT_TRANSFER_OUT_DESCRIPTION = "Transfer to user %d";
    public static final String DEFAULT_TRANSFER_IN_DESCRIPTION = "Transfer from user %d";
    
    public static final int MIN_DESCRIPTION_LENGTH = 1;
    public static final int MAX_DESCRIPTION_LENGTH = 255;
    public static final int REFERENCE_ID_LENGTH = 36;
    
    public static final String AMOUNT_PRECISION_REGEX = "^[0-9]+\\.?[0-9]*$";
    
    private WalletConstants() {
    }
} 