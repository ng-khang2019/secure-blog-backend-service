package com.project.prjauth.controller;

import com.project.prjauth.dto.request.PasswordUpdateRequest;
import com.project.prjauth.dto.request.UserRegistrationRequest;
import com.project.prjauth.dto.request.UserUpdateRequest;
import com.project.prjauth.dto.response.UserDetailsResponse;
import com.project.prjauth.dto.response.UserProfileResponse;
import com.project.prjauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/users")
@RequiredArgsConstructor
public class UserPublicController {
    private final UserService userService;

    // Create user
    @PostMapping("/register")
    public ResponseEntity<UserDetailsResponse> registerUser(@RequestBody UserRegistrationRequest request) {
        UserDetailsResponse createdUser = userService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    // Update the current logged-in user
    @PutMapping("/update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDetailsResponse> updateCurrentUser(
            @RequestBody UserUpdateRequest request
    ) {
        UserDetailsResponse updatedUser= userService.updateUser(request);
        return ResponseEntity.ok(updatedUser);
    }

    // Update the password of the current logged-in user
    @PutMapping("/update-password")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateCurrentUserPassword(@RequestBody PasswordUpdateRequest request) {
        userService.updatePassword(request);
        return ResponseEntity.noContent().build();
    }

    // Load the current user's profile
    @GetMapping("/my-profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    // Load user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserProfileById(id));
    }
}
