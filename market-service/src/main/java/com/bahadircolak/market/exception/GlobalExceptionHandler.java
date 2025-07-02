package com.bahadircolak.market.exception;

import com.bahadircolak.market.constants.ErrorMessages;
import com.bahadircolak.market.dto.response.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AssetNotFoundException.class)
    public ResponseEntity<MessageResponse> handleAssetNotFound(AssetNotFoundException ex) {
        MessageResponse response = new MessageResponse(ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MarketDataException.class)
    public ResponseEntity<MessageResponse> handleMarketDataException(MarketDataException ex) {
        MessageResponse response = new MessageResponse(ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<MessageResponse> handleValidationException(ValidationException ex) {
        MessageResponse response = new MessageResponse(ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MarketException.class)
    public ResponseEntity<MessageResponse> handleMarketException(MarketException ex) {
        MessageResponse response = new MessageResponse(ex.getMessage(), false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleGenericException(Exception ex) {
        MessageResponse response = new MessageResponse(ErrorMessages.GENERAL_ERROR, false);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 