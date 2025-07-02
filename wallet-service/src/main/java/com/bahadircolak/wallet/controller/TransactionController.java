package com.bahadircolak.wallet.controller;

import com.bahadircolak.wallet.constants.WalletConstants;
import com.bahadircolak.wallet.dto.response.TransactionSummaryResponse;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.service.IWalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = WalletConstants.CROSS_ORIGIN_PATTERN, maxAge = WalletConstants.CROSS_ORIGIN_MAX_AGE)
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final IWalletTransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<WalletTransaction>> getUserTransactions() {
        List<WalletTransaction> transactions = transactionService.getUserTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<WalletTransaction>> getUserTransactionsByType(@PathVariable TransactionType type) {
        List<WalletTransaction> transactions = transactionService.getUserTransactionsByType(type);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/range")
    public ResponseEntity<List<WalletTransaction>> getUserTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<WalletTransaction> transactions = transactionService.getUserTransactionsByDateRange(startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryResponse> getTransactionSummary() {
        TransactionSummaryResponse summary = transactionService.getTransactionSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WalletTransaction>> getTransactionsByUserId(@PathVariable Long userId) {
        List<WalletTransaction> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }
} 