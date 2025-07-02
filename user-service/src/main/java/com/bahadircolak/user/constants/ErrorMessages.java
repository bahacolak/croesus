package com.bahadircolak.user.constants;

public final class ErrorMessages {
    
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_NOT_FOUND_BY_ID = "User not found with ID: %d";
    public static final String USER_NOT_FOUND_BY_USERNAME = "User not found with username: %s";
    public static final String USER_NOT_FOUND_BY_EMAIL = "User not found with email: %s";
    public static final String CURRENT_USER_NOT_FOUND = "Current user not found";
    
    public static final String USERNAME_ALREADY_EXISTS = "Username is already taken";
    public static final String EMAIL_ALREADY_EXISTS = "Email is already in use";
    
    public static final String USERNAME_REQUIRED = "Username is required";
    public static final String USERNAME_INVALID_LENGTH = "Username must be between %d and %d characters";
    public static final String USERNAME_INVALID_FORMAT = "Username can only contain letters, numbers, and underscores";
    
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_INVALID_LENGTH = "Password must be between %d and %d characters";
    
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_INVALID_FORMAT = "Invalid email format";
    
    public static final String FIRST_NAME_REQUIRED = "First name is required";
    public static final String FIRST_NAME_INVALID_LENGTH = "First name must be between %d and %d characters";
    public static final String FIRST_NAME_INVALID_FORMAT = "First name can only contain letters and spaces";
    
    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_INVALID_LENGTH = "Last name must be between %d and %d characters";
    public static final String LAST_NAME_INVALID_FORMAT = "Last name can only contain letters and spaces";
    
    public static final String ROLE_NOT_FOUND = "Role not found";
    public static final String INVALID_CREDENTIALS = "Invalid username or password";
    public static final String JWT_TOKEN_INVALID = "Invalid JWT token";
    public static final String JWT_TOKEN_EXPIRED = "JWT token is expired";
    public static final String JWT_TOKEN_UNSUPPORTED = "JWT token is unsupported";
    public static final String JWT_CLAIMS_EMPTY = "JWT claims string is empty";
    
    public static final String ACCESS_DENIED = "Access denied";
    public static final String UNAUTHORIZED = "Unauthorized access";
    
    public static final String GENERAL_ERROR = "An unexpected error occurred";
    
    private ErrorMessages() {
    }
} 