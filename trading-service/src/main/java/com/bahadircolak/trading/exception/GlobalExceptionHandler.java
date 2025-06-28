package com.bahadircolak.trading.exception;

import com.bahadircolak.trading.dto.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AssetNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleAssetNotFoundException(AssetNotFoundException e) {
        log.error("Asset not found: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new MessageResponse(e.getMessage(), false));
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleInsufficientBalanceException(InsufficientBalanceException e) {
        log.error("Insufficient balance: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new MessageResponse(e.getMessage(), false));
    }

    @ExceptionHandler(InsufficientAssetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleInsufficientAssetException(InsufficientAssetException e) {
        log.error("Insufficient asset: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(new MessageResponse(e.getMessage(), false));
    }

    @ExceptionHandler(TradingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<MessageResponse> handleTradingException(TradingException e) {
        log.error("Trading error [{}]: {}", e.getErrorCode(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(new MessageResponse(e.getMessage(), false));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<MessageResponse> handleGenericException(Exception e) {
        log.error("Unexpected error occurred: {}", e.getMessage(), e);
        return ResponseEntity.internalServerError()
                .body(new MessageResponse("An unexpected error occurred. Please try again later.", false));
    }
} 