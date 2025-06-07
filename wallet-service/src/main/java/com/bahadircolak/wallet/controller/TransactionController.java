package com.bahadircolak.wallet.controller;

import com.bahadircolak.wallet.dto.response.TransactionSummaryResponse;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Slf4j
public class TransactionController {

    private final WalletTransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<WalletTransaction>> getUserTransactions() {
        try {
            List<WalletTransaction> transactions = transactionService.getUserTransactions();
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching user transactions", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<WalletTransaction>> getUserTransactionsByType(@PathVariable TransactionType type) {
        try {
            List<WalletTransaction> transactions = transactionService.getUserTransactionsByType(type);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching user transactions by type: {}", type, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/range")
    public ResponseEntity<List<WalletTransaction>> getUserTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<WalletTransaction> transactions = transactionService.getUserTransactionsByDateRange(startDate, endDate);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching user transactions by date range", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/summary")
    public ResponseEntity<TransactionSummaryResponse> getTransactionSummary() {
        try {
            TransactionSummaryResponse summary = transactionService.getTransactionSummary();
            return ResponseEntity.ok(summary);
        } catch (Exception e) {
            log.error("Error fetching transaction summary", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WalletTransaction>> getTransactionsByUserId(@PathVariable Long userId) {
        try {
            List<WalletTransaction> transactions = transactionService.getTransactionsByUserId(userId);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            log.error("Error fetching transactions for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }
} 