package com.bahadircolak.wallet.constants;

public final class ErrorMessages {
    
    public static final String WALLET_NOT_FOUND = "Wallet not found";
    public static final String WALLET_NOT_FOUND_FOR_USER = "Wallet not found for user: %d";
    public static final String WALLET_ALREADY_EXISTS = "Wallet already exists for user: %d";
    public static final String WALLET_INACTIVE = "Wallet is inactive for user: %d";
    
    public static final String USER_NOT_FOUND = "User not found: %d";
    public static final String TARGET_USER_NOT_FOUND = "Target user not found: %d";
    public static final String USER_ID_REQUIRED = "User ID is required";
    public static final String USER_ID_INVALID = "Invalid user ID";
    
    public static final String INSUFFICIENT_BALANCE = "Insufficient balance. Current balance: %s, Requested: %s";
    public static final String INSUFFICIENT_BALANCE_SIMPLE = "Insufficient balance";
    public static final String BALANCE_CANNOT_BE_NEGATIVE = "Balance cannot be negative";
    
    public static final String AMOUNT_REQUIRED = "Amount is required";
    public static final String AMOUNT_INVALID = "Invalid amount";
    public static final String AMOUNT_TOO_SMALL = "Amount must be at least %s";
    public static final String AMOUNT_TOO_LARGE = "Amount cannot exceed %s";
    public static final String AMOUNT_NEGATIVE = "Amount cannot be negative";
    public static final String AMOUNT_ZERO = "Amount cannot be zero";
    
    public static final String SELF_TRANSFER_NOT_ALLOWED = "Cannot transfer to yourself";
    public static final String TRANSFER_TO_SAME_WALLET = "Cannot transfer to the same wallet";
    
    public static final String DESCRIPTION_TOO_LONG = "Description cannot exceed %d characters";
    public static final String REFERENCE_ID_INVALID = "Invalid reference ID format";
    
    public static final String TRANSACTION_TYPE_REQUIRED = "Transaction type is required";
    public static final String TRANSACTION_NOT_FOUND = "Transaction not found";
    public static final String TRANSACTION_FAILED = "Transaction failed";
    public static final String TRANSACTION_CANCELLED = "Transaction cancelled";
    
    public static final String DATE_RANGE_INVALID = "Invalid date range";
    public static final String START_DATE_AFTER_END_DATE = "Start date cannot be after end date";
    public static final String DATE_REQUIRED = "Date is required";
    
    public static final String GENERAL_ERROR = "An unexpected error occurred";
    
    private ErrorMessages() {
    }
} 