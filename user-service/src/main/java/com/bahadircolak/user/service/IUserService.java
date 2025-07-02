package com.bahadircolak.user.service;

import com.bahadircolak.user.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    
    List<User> getAllUsers();
    
    Optional<User> getUserById(Long id);
    
    Optional<User> getUserByUsername(String username);
    
    Optional<User> getUserByEmail(String email);
    
    User getCurrentUser();
    
    User updateUser(User user);
    
    void deleteUser(Long id);
} 