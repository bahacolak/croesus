package com.bahadircolak.user.config;

import com.bahadircolak.user.model.Role;
import com.bahadircolak.user.model.Role.ERole;
import com.bahadircolak.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        loadRoles();
    }

    private void loadRoles() {
        if (roleRepository.count() == 0) {
            log.info("Loading roles...");
            
            Role userRole = new Role();
            userRole.setName(ERole.ROLE_USER);
            
            Role adminRole = new Role();
            adminRole.setName(ERole.ROLE_ADMIN);
            
            roleRepository.saveAll(Arrays.asList(userRole, adminRole));
            
            log.info("Roles loaded successfully.");
        } else {
            log.info("Roles already exist in database, skipping role initialization.");
        }
    }
} 