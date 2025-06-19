package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.dto.response.TransactionSummaryResponse;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionStatus;
import com.bahadircolak.wallet.repository.WalletTransactionRepository;
import com.bahadircolak.common.client.UserClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletTransactionService {

    private final WalletTransactionRepository transactionRepository;
    private final UserClient userClient;

    public WalletTransaction saveTransaction(WalletTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<WalletTransaction> getUserTransactions() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userClient.getUserIdByUsername(username);
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    public List<WalletTransaction> getUserTransactionsByType(TransactionType type) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userClient.getUserIdByUsername(username);
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    public List<WalletTransaction> getUserTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userClient.getUserIdByUsername(username);
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
    }

    public TransactionSummaryResponse getTransactionSummary() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userClient.getUserIdByUsername(username);

        TransactionSummaryResponse summary = new TransactionSummaryResponse();

        // Calculate total amounts
        summary.setTotalDeposits(getTotalAmountByTypeAndStatus(userId, TransactionType.DEPOSIT, TransactionStatus.COMPLETED));
        summary.setTotalWithdrawals(getTotalAmountByTypeAndStatus(userId, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED));
        summary.setTotalTransfersIn(getTotalAmountByTypeAndStatus(userId, TransactionType.TRANSFER_IN, TransactionStatus.COMPLETED));
        summary.setTotalTransfersOut(getTotalAmountByTypeAndStatus(userId, TransactionType.TRANSFER_OUT, TransactionStatus.COMPLETED));

        // Calculate transaction counts
        summary.setDepositCount(getTransactionCountByType(userId, TransactionType.DEPOSIT));
        summary.setWithdrawalCount(getTransactionCountByType(userId, TransactionType.WITHDRAWAL));
        summary.setTransferCount(
                getTransactionCountByType(userId, TransactionType.TRANSFER_IN) + 
                getTransactionCountByType(userId, TransactionType.TRANSFER_OUT)
        );

        // Calculate net amount (in - out)
        BigDecimal totalIn = summary.getTotalDeposits().add(summary.getTotalTransfersIn());
        BigDecimal totalOut = summary.getTotalWithdrawals().add(summary.getTotalTransfersOut());
        summary.setNetAmount(totalIn.subtract(totalOut));

        return summary;
    }

    public List<WalletTransaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    private BigDecimal getTotalAmountByTypeAndStatus(Long userId, TransactionType type, TransactionStatus status) {
        BigDecimal total = transactionRepository.getTotalAmountByUserIdAndTypeAndStatus(userId, type, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    private Long getTransactionCountByType(Long userId, TransactionType type) {
        return transactionRepository.countByUserIdAndType(userId, type);
    }
} 