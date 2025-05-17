package com.bahadircolak.croesus.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletTransactionRequest {
    
    @NotNull(message = "Transaction amount cannot be empty")
    @DecimalMin(value = "0.01", message = "Transaction amount must be at least 0.01")
    private BigDecimal amount;
    
    private String description;
} 