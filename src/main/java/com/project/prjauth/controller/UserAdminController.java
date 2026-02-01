package com.project.prjauth.controller;

import com.project.prjauth.dto.request.UserUpdateRequest;
import com.project.prjauth.dto.response.UserDetailsResponse;
import com.project.prjauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserService userService;

    // Load a specific user (with full details)
    @GetMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserDetailsById(id));
    }

    // Update a user by id
    @PutMapping("/{id}")
    public ResponseEntity<UserDetailsResponse> updateUser(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        UserDetailsResponse updatedUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updatedUser);
    }

    // Load all users (with full details)
    @GetMapping
    public ResponseEntity<Page<UserDetailsResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "lastName") String sortBy,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return ResponseEntity.ok(userService.getUsers(pageable));
    }

    // Delete user with authorization
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }
}
