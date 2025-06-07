package com.bahadircolak.wallet.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryResponse {
    private BigDecimal totalDeposits;
    private BigDecimal totalWithdrawals;
    private BigDecimal totalTransfersIn;
    private BigDecimal totalTransfersOut;
    private Long depositCount;
    private Long withdrawalCount;
    private Long transferCount;
    private BigDecimal netAmount;
} 