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
public class AuthService implements IAuthService {

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
        UserDetailsImpl userDetails = extractUserDetails(authentication);
        List<String> roles = extractRoles(userDetails);

        return buildJwtResponse(jwt, userDetails, roles);
    }

    @Override
    public MessageResponse registerUser(SignupRequest signupRequest) {
        validator.validateSignupRequest(signupRequest);
        checkUserExistence(signupRequest);
        
        User user = createUserFromRequest(signupRequest);
        Set<Role> roles = determineUserRoles(signupRequest.getRole());
        user.setRoles(roles);
        
        saveUser(user);
        return new MessageResponse(SuccessMessages.USER_REGISTERED_SUCCESSFULLY, true);
    }

    private Authentication performAuthentication(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(), 
                loginRequest.getPassword()
            )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    private String generateJwtToken(Authentication authentication) {
        return jwtUtils.generateJwtToken(authentication);
    }

    private UserDetailsImpl extractUserDetails(Authentication authentication) {
        return (UserDetailsImpl) authentication.getPrincipal();
    }

    private List<String> extractRoles(UserDetailsImpl userDetails) {
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

    private JwtResponse buildJwtResponse(String jwt, UserDetailsImpl userDetails, List<String> roles) {
        return new JwtResponse(
            jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getFirstName(),
            userDetails.getLastName(),
            roles
        );
    }

    private void checkUserExistence(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new UserAlreadyExistsException(ErrorMessages.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new UserAlreadyExistsException(ErrorMessages.EMAIL_ALREADY_EXISTS);
        }
    }

    private User createUserFromRequest(SignupRequest signupRequest) {
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(encoder.encode(signupRequest.getPassword()));
        user.setFirstName(signupRequest.getFirstName());
        user.setLastName(signupRequest.getLastName());
        return user;
    }

    private Set<Role> determineUserRoles(Set<String> requestRoles) {
        Set<Role> roles = new HashSet<>();

        if (requestRoles == null || requestRoles.isEmpty()) {
            roles.add(getUserRole());
        } else {
            requestRoles.forEach(role -> {
                if (UserConstants.ROLE_ADMIN_STRING.equals(role)) {
                    roles.add(getAdminRole());
                } else {
                    roles.add(getUserRole());
                }
            });
        }

        return roles;
    }

    private Role getUserRole() {
        return roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new UserException(ErrorMessages.ROLE_NOT_FOUND));
    }

    private Role getAdminRole() {
        return roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new UserException(ErrorMessages.ROLE_NOT_FOUND));
    }

    private void saveUser(User user) {
        userRepository.save(user);
    }
} 