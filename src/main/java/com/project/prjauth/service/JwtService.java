package com.project.prjauth.service;

import com.project.prjauth.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;

public interface JwtService {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    void verifyToken(String token);
    String extractJti(String token);
    String extractUsername(String token);
    Instant extractExpiration(String token);
    String buildScope(User user);
}
