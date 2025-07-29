package com.bahadircolak.user.service;

import com.bahadircolak.user.constants.ErrorMessages;
import com.bahadircolak.user.exception.UserNotFoundException;
import com.bahadircolak.user.model.User;
import com.bahadircolak.user.repository.UserRepository;
import com.bahadircolak.user.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator validator;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        validator.validateUserId(id);
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        validator.validateUsername(username);
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        validator.validateEmail(email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User getCurrentUser() {
        UserDetails userDetails = getCurrentUserDetails();
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UserNotFoundException(ErrorMessages.CURRENT_USER_NOT_FOUND));
    }

    @Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        validator.validateUserId(id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(String.format(ErrorMessages.USER_NOT_FOUND_BY_ID, id));
        }
        userRepository.deleteById(id);
    }

    private UserDetails getCurrentUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
} 