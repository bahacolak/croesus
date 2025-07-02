package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.dto.response.TransactionSummaryResponse;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;

import java.time.LocalDateTime;
import java.util.List;

public interface IWalletTransactionService {
    
    WalletTransaction saveTransaction(WalletTransaction transaction);
    
    List<WalletTransaction> getUserTransactions();
    
    List<WalletTransaction> getUserTransactionsByType(TransactionType type);
    
    List<WalletTransaction> getUserTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    TransactionSummaryResponse getTransactionSummary();
    
    List<WalletTransaction> getTransactionsByUserId(Long userId);
} 