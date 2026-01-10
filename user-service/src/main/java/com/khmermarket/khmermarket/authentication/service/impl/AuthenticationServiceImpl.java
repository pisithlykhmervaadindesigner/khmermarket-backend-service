package com.khmermarket.khmermarket.authentication.service.impl;

import com.khmermarket.khmermarket.config.JwtService;
import com.khmermarket.khmermarket.authentication.request.AuthenticationRequest;
import com.khmermarket.khmermarket.authentication.request.RegisterRequest;
import com.khmermarket.khmermarket.authentication.response.AuthenticationResponse;
import com.khmermarket.khmermarket.domain.dto.UserDto;
import com.khmermarket.khmermarket.domain.entity.User;
import com.khmermarket.khmermarket.domain.entity.UserRole;
import com.khmermarket.khmermarket.domain.repository.UserRepository;
import com.khmermarket.khmermarket.domain.repository.UserRoleRepository;
import com.khmermarket.khmermarket.authentication.service.AuthenticationService;
import com.khmermarket.khmermarket.enumerate.RoleName;
import com.khmermarket.khmermarket.enumerate.UserStatus;
import com.khmermarket.khmermarket.security.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRoleRepository userRoleRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // If authentication is successful, generate a token
        var user = userRepository.findByEmail(request.getEmail())
                .or(() -> userRepository.findByUsername(request.getEmail()))
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserRole> userRoleList = roleRepository.findAllByUserId(user.getId());

        UserPrinciple userPrinciple = new UserPrinciple(user, userRoleList);
        var jwtToken = jwtService.generateToken(userPrinciple);
        return buildAuthResponse(user, jwtToken);
    }

    private AuthenticationResponse buildAuthResponse(User user, String jwtToken) {
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .expiresIn(jwtService.getJwtExpiration())
                .build();
    }

    @Override
    @Transactional
    public UserDto register(RegisterRequest request) {
        System.out.println("=== REGISTER METHOD CALLED ===");
        System.out.println("Username: " + request.getUsername());
        System.out.println("Email: " + request.getEmail());
        
        // Check if user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (request.getPhoneNumber() != null && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number already exists");
        }

        // Get default USER role
        System.out.println("Looking for USER role...");
        System.out.println("All available roles:");
        userRoleRepository.findAll().forEach(role -> 
            System.out.println(" - " + role.getName() + " (ID: " + role.getId() + ")")
        );
        
        UserRole userRole = userRoleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> {
                    System.out.println("USER role not found!");
                    return new RuntimeException("Default USER role not found");
                });
        System.out.println("Found USER role: " + userRole.getName());
        System.out.println("UserRole ID: " + userRole.getId());

        // Create a new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);
        System.out.println("User saved successfully with ID: " + savedUser.getId());
        
        return convertToDto(savedUser);
    }

    private UserDto convertToDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}