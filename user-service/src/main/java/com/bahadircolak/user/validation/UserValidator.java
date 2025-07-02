package com.bahadircolak.user.validation;

import com.bahadircolak.user.constants.ErrorMessages;
import com.bahadircolak.user.constants.UserConstants;
import com.bahadircolak.user.dto.request.LoginRequest;
import com.bahadircolak.user.dto.request.SignupRequest;
import com.bahadircolak.user.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validateLoginRequest(LoginRequest request) {
        validateUsername(request.getUsername());
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.PASSWORD_REQUIRED);
        }
    }

    public void validateSignupRequest(SignupRequest request) {
        validateUsername(request.getUsername());
        validatePassword(request.getPassword());
        validateEmail(request.getEmail());
        validateFirstName(request.getFirstName());
        validateLastName(request.getLastName());
    }

    public void validateUserId(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("Invalid user ID");
        }
    }

    public void validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.USERNAME_REQUIRED);
        }
        
        String trimmedUsername = username.trim();
        
        if (trimmedUsername.length() < UserConstants.MIN_USERNAME_LENGTH || 
            trimmedUsername.length() > UserConstants.MAX_USERNAME_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.USERNAME_INVALID_LENGTH, 
                UserConstants.MIN_USERNAME_LENGTH, 
                UserConstants.MAX_USERNAME_LENGTH
            ));
        }
        
        if (!trimmedUsername.matches(UserConstants.USERNAME_REGEX)) {
            throw new ValidationException(ErrorMessages.USERNAME_INVALID_FORMAT);
        }
    }

    public void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.PASSWORD_REQUIRED);
        }
        
        if (password.length() < UserConstants.MIN_PASSWORD_LENGTH || 
            password.length() > UserConstants.MAX_PASSWORD_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.PASSWORD_INVALID_LENGTH, 
                UserConstants.MIN_PASSWORD_LENGTH, 
                UserConstants.MAX_PASSWORD_LENGTH
            ));
        }
    }

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.EMAIL_REQUIRED);
        }
        
        if (!email.trim().matches(UserConstants.EMAIL_REGEX)) {
            throw new ValidationException(ErrorMessages.EMAIL_INVALID_FORMAT);
        }
    }

    public void validateFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.FIRST_NAME_REQUIRED);
        }
        
        String trimmedName = firstName.trim();
        
        if (trimmedName.length() < UserConstants.MIN_NAME_LENGTH || 
            trimmedName.length() > UserConstants.MAX_NAME_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.FIRST_NAME_INVALID_LENGTH, 
                UserConstants.MIN_NAME_LENGTH, 
                UserConstants.MAX_NAME_LENGTH
            ));
        }
        
        if (!trimmedName.matches(UserConstants.NAME_REGEX)) {
            throw new ValidationException(ErrorMessages.FIRST_NAME_INVALID_FORMAT);
        }
    }

    public void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException(ErrorMessages.LAST_NAME_REQUIRED);
        }
        
        String trimmedName = lastName.trim();
        
        if (trimmedName.length() < UserConstants.MIN_NAME_LENGTH || 
            trimmedName.length() > UserConstants.MAX_NAME_LENGTH) {
            throw new ValidationException(String.format(
                ErrorMessages.LAST_NAME_INVALID_LENGTH, 
                UserConstants.MIN_NAME_LENGTH, 
                UserConstants.MAX_NAME_LENGTH
            ));
        }
        
        if (!trimmedName.matches(UserConstants.NAME_REGEX)) {
            throw new ValidationException(ErrorMessages.LAST_NAME_INVALID_FORMAT);
        }
    }
} 