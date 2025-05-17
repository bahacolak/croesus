package com.bahadircolak.croesus.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRequest {
    
    @NotBlank(message = "Asset symbol cannot be empty")
    private String symbol;
    
    @NotNull(message = "Quantity cannot be empty")
    @DecimalMin(value = "0.000001", message = "Quantity must be at least 0.000001")
    private BigDecimal quantity;
    
    private String description;
} 