package com.khmermarket.khmermarket.controller;

import com.khmermarket.khmermarket.domain.dto.UserDto;
import com.khmermarket.khmermarket.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private final UserService userService;

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
