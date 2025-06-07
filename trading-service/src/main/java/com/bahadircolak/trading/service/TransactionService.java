package com.bahadircolak.trading.service;

import com.bahadircolak.trading.model.Transaction;
import com.bahadircolak.trading.model.Transaction.TransactionType;
import com.bahadircolak.trading.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;

    public List<Transaction> getUserTransactions() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    public List<Transaction> getUserTransactionsByType(TransactionType type) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    public List<Transaction> getUserTransactionsByAsset(Long assetId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        return transactionRepository.findByUserIdAndAssetId(userId, assetId);
    }

    public BigDecimal getTotalAmountByType(TransactionType type) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userService.getUserIdByUsername(userDetails.getUsername());
        BigDecimal total = transactionRepository.getTotalAmountByUserIdAndType(userId, type);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<Transaction> getTransactionsBetweenDates(LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
} 