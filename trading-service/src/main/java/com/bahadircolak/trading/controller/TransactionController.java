package com.bahadircolak.trading.controller;

import com.bahadircolak.trading.model.Transaction;
import com.bahadircolak.trading.model.Transaction.TransactionType;
import com.bahadircolak.trading.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<Transaction>> getUserTransactions() {
        return ResponseEntity.ok(transactionService.getUserTransactions());
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByType(@PathVariable("type") TransactionType type) {
        return ResponseEntity.ok(transactionService.getUserTransactionsByType(type));
    }

    @GetMapping("/asset/{assetId}")
    public ResponseEntity<List<Transaction>> getUserTransactionsByAsset(@PathVariable("assetId") Long assetId) {
        return ResponseEntity.ok(transactionService.getUserTransactionsByAsset(assetId));
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, BigDecimal>> getTransactionSummary() {
        return ResponseEntity.ok(Map.of(
                "totalBuyAmount", transactionService.getTotalAmountByType(TransactionType.BUY),
                "totalSellAmount", transactionService.getTotalAmountByType(TransactionType.SELL),
                "totalDepositAmount", transactionService.getTotalAmountByType(TransactionType.DEPOSIT),
                "totalWithdrawalAmount", transactionService.getTotalAmountByType(TransactionType.WITHDRAWAL)
        ));
    }

    @GetMapping("/between")
    public ResponseEntity<List<Transaction>> getTransactionsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(transactionService.getTransactionsBetweenDates(startDate, endDate));
    }
} 