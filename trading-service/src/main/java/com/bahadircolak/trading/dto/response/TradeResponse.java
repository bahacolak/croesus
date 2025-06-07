package com.bahadircolak.trading.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TradeResponse {
    
    private String message;
    private String assetSymbol;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalAmount;
    private String transactionType;
    private LocalDateTime transactionDate;
} 