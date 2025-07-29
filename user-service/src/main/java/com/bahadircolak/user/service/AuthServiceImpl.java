package com.bahadircolak.user.service;

import com.bahadircolak.user.constants.ErrorMessages;
import com.bahadircolak.user.constants.SuccessMessages;
import com.bahadircolak.user.constants.UserConstants;
import com.bahadircolak.user.dto.request.LoginRequest;
import com.bahadircolak.user.dto.request.SignupRequest;
import com.bahadircolak.user.dto.response.JwtResponse;
import com.bahadircolak.user.dto.response.MessageResponse;
import com.bahadircolak.user.exception.UserAlreadyExistsException;
import com.bahadircolak.user.exception.UserException;
import com.bahadircolak.user.model.Role;
import com.bahadircolak.user.model.Role.ERole;
import com.bahadircolak.user.model.User;
import com.bahadircolak.user.repository.RoleRepository;
import com.bahadircolak.user.repository.UserRepository;
import com.bahadircolak.user.security.jwt.JwtUtils;
import com.bahadircolak.user.security.service.UserDetailsImpl;
import com.bahadircolak.user.validation.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;
    private final UserValidator validator;

    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        validator.validateLoginRequest(loginRequest);
        
        Authentication authentication = performAuthentication(loginRequest);
        String jwt = generateJwtToken(authentication);
        
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = getUserRoles(userDetails);
        
        return createJwtResponse(jwt, userDetails, roles);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signupRequest) {
        validator.validateSignupRequest(signupRequest);
        
        validateUserUniqueness(signupRequest);
        
        User user = createUser(signupRequest);
        userRepository.save(user);
        
        return new MessageResponse(SuccessMessages.USER_REGISTERED_SUCCESSFULLY);
    }

    private Authentication performAuthentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(), 
                        loginRequest.getPassword()));
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    private List<String> getUserRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private JwtResponse createJwtResponse(String jwt, UserDetailsImpl userDetails, List<String> roles) {
        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getFirstName(),
                userDetails.getLastName(),
                roles);
    }

    private void validateUserUniqueness(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserAlreadyExistsException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
    }

    private User createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = determineUserRoles(strRoles);
        user.setRoles(roles);

        return user;
    }

    private Set<Role> determineUserRoles(Set<String> strRoles) {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            addDefaultRole(roles);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case UserConstants.ROLE_ADMIN_STRING:
                        addAdminRole(roles);
                        break;
                    default:
                        addDefaultRole(roles);
                }
            });
        }

        return roles;
    }

    private void addDefaultRole(Set<Role> roles) {
        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new UserException(ErrorMessages.ROLE_NOT_FOUND));
        roles.add(userRole);
    }

    private void addAdminRole(Set<Role> roles) {
        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new UserException(ErrorMessages.ROLE_NOT_FOUND));
        roles.add(adminRole);
    }
} 