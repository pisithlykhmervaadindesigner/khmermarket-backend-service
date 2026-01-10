package com.khmermarket.khmermarket.initializer;

import com.khmermarket.khmermarket.domain.entity.Role;
import com.khmermarket.khmermarket.domain.entity.UserRole;
import com.khmermarket.khmermarket.domain.repository.RoleRepository;
import com.khmermarket.khmermarket.enumerate.RoleName;
import com.khmermarket.khmermarket.domain.repository.UserRoleRepository;
import com.khmermarket.khmermarket.enumerate.UserStatus;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class RoleInitializer {
    
    private final RoleRepository roleRepository;
    
    @PostConstruct
    public void init() {
        System.out.println("Initializing roles...");
        for (RoleName roleName : RoleName.values()) {
            System.out.println("Checking role: " + roleName);
            if (!roleRepository.existsByName(roleName)) {
                System.out.println("Creating role: " + roleName);
                Role userRole = Role.builder()
                    .name(roleName)
                    .description(roleName.name() + " role with default permissions")
                    .createdAt(LocalDateTime.now())
                    .status(UserStatus.ACTIVE)
                    .build();
                roleRepository.save(userRole);
                System.out.println("Created role: " + roleName);
            } else {
                System.out.println("Role already exists: " + roleName);
            }
        }
        System.out.println("Role initialization completed.");
    }
}
