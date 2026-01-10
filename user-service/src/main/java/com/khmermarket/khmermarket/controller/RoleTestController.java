package com.khmermarket.khmermarket.controller;

import com.khmermarket.khmermarket.domain.entity.Role;
import com.khmermarket.khmermarket.domain.entity.User;
import com.khmermarket.khmermarket.domain.repository.RoleRepository;
import com.khmermarket.khmermarket.domain.repository.UserRepository;
import com.khmermarket.khmermarket.enumerate.RoleName;
import com.khmermarket.khmermarket.enumerate.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class RoleTestController {
    
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
    
    @GetMapping("/create-user")
    public User createTestUser() {
        System.err.println("=== Creating test user ===");
        
        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("USER role not found"));
        
        System.err.println("Found role: " + userRole.getName() + " with ID: " + userRole.getId());

        User user = User.builder()
                .username("testuser_manual")
                .email("testmanual@example.com")
                .passwordHash(passwordEncoder.encode("password"))
                .phoneNumber("+85512345699")
                .status(UserStatus.ACTIVE)
                .build();

        return userRepository.save(user);
    }
}
