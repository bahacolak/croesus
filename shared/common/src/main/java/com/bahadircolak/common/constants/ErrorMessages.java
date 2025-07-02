package com.bahadircolak.common.constants;

public final class ErrorMessages {
    
    public static final String USER_NOT_FOUND = "User not found: %s";
    public static final String USER_ID_NOT_FOUND = "User ID not found for username: %s";
    public static final String USER_SERVICE_UNAVAILABLE = "User service is unavailable";
    public static final String WALLET_SERVICE_UNAVAILABLE = "Wallet service is unavailable";
    
    public static final String BALANCE_FETCH_ERROR = "Error fetching wallet balance for user: %d";
    public static final String BALANCE_UPDATE_ERROR = "Error updating wallet balance for user: %d";
    
    public static final String INVALID_USER_ID = "Invalid user ID: %d";
    public static final String INVALID_USERNAME = "Invalid username: %s";
    public static final String INVALID_AMOUNT = "Invalid amount: %s";
    
    public static final String AUTH_TOKEN_MISSING = "Authorization token is missing";
    public static final String AUTH_TOKEN_INVALID = "Invalid authorization token";
    
    public static final String SERVICE_COMMUNICATION_ERROR = "Service communication error";
    public static final String GENERAL_ERROR = "An unexpected error occurred";
    
    private ErrorMessages() {
    }
} 