package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.dto.response.MessageResponse;
import com.bahadircolak.wallet.dto.response.WalletResponse;

import java.math.BigDecimal;

public interface WalletService {
    
    WalletResponse createWallet(Long userId);
    
    WalletResponse getWalletByUserId(Long userId);
    
    BigDecimal getBalance(Long userId);
    
    WalletResponse getCurrentUserWallet();
    
    MessageResponse deposit(WalletTransactionRequest request);
    
    MessageResponse withdraw(WalletTransactionRequest request);
    
    MessageResponse transfer(TransferRequest request);
    
    void updateBalance(Long userId, BigDecimal amount);
} 