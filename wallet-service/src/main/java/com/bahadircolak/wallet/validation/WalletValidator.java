package com.bahadircolak.wallet.validation;

import com.bahadircolak.wallet.constants.ErrorMessages;
import com.bahadircolak.wallet.constants.WalletConstants;
import com.bahadircolak.wallet.dto.request.TransferRequest;
import com.bahadircolak.wallet.dto.request.WalletTransactionRequest;
import com.bahadircolak.wallet.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class WalletValidator {

    public void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException(ErrorMessages.USER_ID_INVALID);
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new ValidationException(ErrorMessages.AMOUNT_REQUIRED);
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException(ErrorMessages.AMOUNT_ZERO);
        }

        if (amount.compareTo(WalletConstants.MIN_TRANSACTION_AMOUNT) < 0) {
            throw new ValidationException(String.format(
                ErrorMessages.AMOUNT_TOO_SMALL, 
                WalletConstants.MIN_TRANSACTION_AMOUNT
            ));
        }

        if (amount.compareTo(WalletConstants.MAX_TRANSACTION_AMOUNT) > 0) {
            throw new ValidationException(String.format(
                ErrorMessages.AMOUNT_TOO_LARGE, 
                WalletConstants.MAX_TRANSACTION_AMOUNT
            ));
        }
    }

    public void validateTransactionRequest(WalletTransactionRequest request) {
        if (request == null) {
            throw new ValidationException("Transaction request is required");
        }

        validateAmount(request.getAmount());
        validateDescription(request.getDescription());
        validateReferenceId(request.getReferenceId());
    }

    public void validateTransferRequest(TransferRequest request) {
        if (request == null) {
            throw new ValidationException("Transfer request is required");
        }

        validateAmount(request.getAmount());
        validateUserId(request.getTargetUserId());
        validateDescription(request.getDescription());
    }

    public void validateDescription(String description) {
        if (description != null) {
            if (description.trim().isEmpty()) {
                throw new ValidationException("Description cannot be empty");
            }
            
            if (description.length() > WalletConstants.MAX_DESCRIPTION_LENGTH) {
                throw new ValidationException(String.format(
                    ErrorMessages.DESCRIPTION_TOO_LONG, 
                    WalletConstants.MAX_DESCRIPTION_LENGTH
                ));
            }
        }
    }

    public void validateReferenceId(String referenceId) {
        if (referenceId != null) {
            if (referenceId.trim().isEmpty()) {
                throw new ValidationException("Reference ID cannot be empty");
            }
            
            if (referenceId.length() > WalletConstants.REFERENCE_ID_LENGTH) {
                throw new ValidationException(ErrorMessages.REFERENCE_ID_INVALID);
            }
        }
    }

    public void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            throw new ValidationException("Start date is required");
        }
        
        if (endDate == null) {
            throw new ValidationException("End date is required");
        }
        
        if (startDate.isAfter(endDate)) {
            throw new ValidationException(ErrorMessages.START_DATE_AFTER_END_DATE);
        }
    }

    public void validateBalance(BigDecimal currentBalance, BigDecimal amount) {
        if (currentBalance.compareTo(amount) < 0) {
            throw new ValidationException(String.format(
                ErrorMessages.INSUFFICIENT_BALANCE, 
                currentBalance, 
                amount
            ));
        }
    }

    public void validateSelfTransfer(Long fromUserId, Long toUserId) {
        if (fromUserId.equals(toUserId)) {
            throw new ValidationException(ErrorMessages.SELF_TRANSFER_NOT_ALLOWED);
        }
    }
} 