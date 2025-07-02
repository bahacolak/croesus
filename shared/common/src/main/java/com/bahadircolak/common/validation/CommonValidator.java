package com.bahadircolak.common.validation;

import com.bahadircolak.common.constants.ErrorMessages;
import com.bahadircolak.common.exception.CommonException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class CommonValidator {

    public void validateUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new CommonException(String.format(ErrorMessages.INVALID_USER_ID, userId));
        }
    }

    public void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new CommonException(String.format(ErrorMessages.INVALID_USERNAME, username));
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new CommonException(String.format(ErrorMessages.INVALID_AMOUNT, "null"));
        }
        
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            throw new CommonException(String.format(ErrorMessages.INVALID_AMOUNT, amount));
        }
    }
} 