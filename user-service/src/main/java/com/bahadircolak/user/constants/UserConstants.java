package com.bahadircolak.user.constants;

public final class UserConstants {
    
    public static final String JWT_TOKEN_TYPE = "Bearer";
    public static final String ROLE_USER_STRING = "user";
    public static final String ROLE_ADMIN_STRING = "admin";
    
    public static final String CROSS_ORIGIN_PATTERN = "*";
    public static final long CROSS_ORIGIN_MAX_AGE = 3600;
    
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int MAX_PASSWORD_LENGTH = 40;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;
    
    public static final String USERNAME_REGEX = "^[a-zA-Z0-9_]+$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String NAME_REGEX = "^[a-zA-ZğüşıöçĞÜŞIÖÇ\\s]+$";
    
    private UserConstants() {
    }
} 