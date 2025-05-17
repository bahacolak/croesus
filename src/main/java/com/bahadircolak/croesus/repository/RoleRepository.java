package com.bahadircolak.croesus.repository;

import com.bahadircolak.croesus.model.Role;
import com.bahadircolak.croesus.model.Role.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByName(ERole name);
} 