package com.bahadircolak.wallet.service;

import com.bahadircolak.wallet.dto.response.TransactionSummaryResponse;
import com.bahadircolak.wallet.model.WalletTransaction;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionType;
import com.bahadircolak.wallet.model.WalletTransaction.TransactionStatus;
import com.bahadircolak.wallet.repository.WalletTransactionRepository;
import com.bahadircolak.wallet.validation.WalletValidator;
import com.bahadircolak.common.client.UserClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WalletTransactionService implements IWalletTransactionService {

    private final WalletTransactionRepository transactionRepository;
    private final UserClient userClient;
    private final WalletValidator validator;

    @Override
    public WalletTransaction saveTransaction(WalletTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public List<WalletTransaction> getUserTransactions() {
        Long userId = getCurrentUserId();
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    @Override
    public List<WalletTransaction> getUserTransactionsByType(TransactionType type) {
        Long userId = getCurrentUserId();
        return transactionRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public List<WalletTransaction> getUserTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        validator.validateDateRange(startDate, endDate);
        Long userId = getCurrentUserId();
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate);
    }

    @Override
    public TransactionSummaryResponse getTransactionSummary() {
        Long userId = getCurrentUserId();
        return buildTransactionSummary(userId);
    }

    @Override
    public List<WalletTransaction> getTransactionsByUserId(Long userId) {
        validator.validateUserId(userId);
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId);
    }

    private Long getCurrentUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userClient.getUserIdByUsername(username);
    }

    private TransactionSummaryResponse buildTransactionSummary(Long userId) {
        TransactionSummaryResponse summary = new TransactionSummaryResponse();

        summary.setTotalDeposits(getTotalAmountByTypeAndStatus(userId, TransactionType.DEPOSIT, TransactionStatus.COMPLETED));
        summary.setTotalWithdrawals(getTotalAmountByTypeAndStatus(userId, TransactionType.WITHDRAWAL, TransactionStatus.COMPLETED));
        summary.setTotalTransfersIn(getTotalAmountByTypeAndStatus(userId, TransactionType.TRANSFER_IN, TransactionStatus.COMPLETED));
        summary.setTotalTransfersOut(getTotalAmountByTypeAndStatus(userId, TransactionType.TRANSFER_OUT, TransactionStatus.COMPLETED));

        summary.setDepositCount(getTransactionCountByType(userId, TransactionType.DEPOSIT));
        summary.setWithdrawalCount(getTransactionCountByType(userId, TransactionType.WITHDRAWAL));
        summary.setTransferCount(calculateTotalTransferCount(userId));

        summary.setNetAmount(calculateNetAmount(summary));

        return summary;
    }

    private BigDecimal getTotalAmountByTypeAndStatus(Long userId, TransactionType type, TransactionStatus status) {
        BigDecimal total = transactionRepository.getTotalAmountByUserIdAndTypeAndStatus(userId, type, status);
        return total != null ? total : BigDecimal.ZERO;
    }

    private Long getTransactionCountByType(Long userId, TransactionType type) {
        return transactionRepository.countByUserIdAndType(userId, type);
    }

    private Long calculateTotalTransferCount(Long userId) {
        return getTransactionCountByType(userId, TransactionType.TRANSFER_IN) + 
               getTransactionCountByType(userId, TransactionType.TRANSFER_OUT);
    }

    private BigDecimal calculateNetAmount(TransactionSummaryResponse summary) {
        BigDecimal totalIn = summary.getTotalDeposits().add(summary.getTotalTransfersIn());
        BigDecimal totalOut = summary.getTotalWithdrawals().add(summary.getTotalTransfersOut());
        return totalIn.subtract(totalOut);
    }
} 