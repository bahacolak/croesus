package com.bahadircolak.wallet.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionRequest {
    
    @NotNull(message = "Transaction amount cannot be empty")
    @DecimalMin(value = "0.01", message = "Transaction amount must be at least 0.01")
    private BigDecimal amount;
    
    private String description;
    
    private String referenceId;
} 