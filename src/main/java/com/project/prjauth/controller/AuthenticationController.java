package com.project.prjauth.controller;

import com.project.prjauth.dto.request.LoginRequest;
import com.project.prjauth.dto.response.ApiResponse;
import com.project.prjauth.dto.response.AuthResponse;
import com.project.prjauth.service.AuthenticationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    // Log in
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.signIn(loginRequest, response));
    }

    // Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken, response));
    }

    // Logout
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @CookieValue(name = "access_token") String accessToken,
            @CookieValue(name = "refresh_token") String refreshToken,
            HttpServletResponse response) {
        authenticationService.signOut(accessToken,refreshToken, response);
        return ResponseEntity.noContent().build();
    }
}
