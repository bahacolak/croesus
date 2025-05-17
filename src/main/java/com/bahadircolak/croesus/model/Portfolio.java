package com.bahadircolak.croesus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "portfolios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "asset_id", nullable = false)
    private Asset asset;
    
    @Column(nullable = false)
    private BigDecimal quantity;
    
    @Column(name = "average_buy_price")
    private BigDecimal averageBuyPrice;
    
    @Column(name = "total_investment")
    private BigDecimal totalInvestment;
    
    @Column(name = "current_value")
    private BigDecimal currentValue;
    
    @Column(name = "profit_loss")
    private BigDecimal profitLoss;
    
    @Column(name = "profit_loss_percentage")
    private BigDecimal profitLossPercentage;
} 