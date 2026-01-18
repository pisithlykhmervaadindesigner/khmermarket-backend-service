package com.khmermarket.khmermarket.controller;

import com.khmermarket.khmermarket.domain.dto.UserDto;
import com.khmermarket.khmermarket.domain.entity.User;
import com.khmermarket.khmermarket.domain.repository.UserRepository;
import com.khmermarket.khmermarket.domain.request.CreateUserRequest;
import com.khmermarket.khmermarket.domain.service.UserService;
import com.khmermarket.khmermarket.enumerate.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RuntimeException("Error: Username is already taken!"));
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new RuntimeException("Error: Email is already in use!"));
        }

        // Create a new user
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .status(UserStatus.ACTIVE)
                .build();

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(userService.fromEntityToDto(savedUser));
    }

    @GetMapping
    @Operation(
        summary = "Get all users with pagination",
        description = "Retrieves a paginated list of users"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved list of users",
        content = @Content(schema = @Schema(implementation = Page.class))
    )
    public ResponseEntity<Page<UserDto>> getAllUsers(
            @ParameterObject @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID",
        description = "Retrieves a user by their unique identifier"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved user",
        content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content
    )
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID of the user to be retrieved")
            @PathVariable UUID id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Update an existing user",
        description = "Updates the details of an existing user"
    )
    @ApiResponse(
        responseCode = "200",
        description = "User updated successfully",
        content = @Content(schema = @Schema(implementation = UserDto.class))
    )
    @ApiResponse(
        responseCode = "400",
        description = "Invalid input",
        content = @Content
    )
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content
    )

    @ApiResponse(
        responseCode = "204",
        description = "User deleted successfully",
        content = @Content
    )
    @ApiResponse(
        responseCode = "404",
        description = "User not found",
        content = @Content
    )
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to be deleted")
            @PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
