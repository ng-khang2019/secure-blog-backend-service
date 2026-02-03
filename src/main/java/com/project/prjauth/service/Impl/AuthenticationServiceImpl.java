package com.project.prjauth.service.Impl;

import com.project.prjauth.dto.request.LoginRequest;
import com.project.prjauth.dto.response.AuthResponse;
import com.project.prjauth.entity.CustomUserDetails;
import com.project.prjauth.entity.User;
import com.project.prjauth.exception.ResourceNotFoundException;
import com.project.prjauth.repository.UserRepository;
import com.project.prjauth.service.AuthenticationService;
import com.project.prjauth.service.JwtService;
import com.project.prjauth.service.RedisTokenService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticateManager;
    private final JwtService jwtService;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;

    @Override
    public UserDetails authenticate(String username, String password) {
        Authentication authentication = authenticateManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
        return (UserDetails) authentication.getPrincipal();
    }


    @Override
    public AuthResponse signIn(LoginRequest request, HttpServletResponse response) {
        CustomUserDetails cud = (CustomUserDetails)
                authenticate(request.getUsername(),request.getPassword());
        User user = cud.getUser();

        // Generate tokens
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // Set cookies
        addCookie(response,"access_token",accessToken,30*60);
        addCookie(response, "refresh_token", refreshToken,15*24*60*60);

        // Save the refresh token to Redis
        redisTokenService.saveRefreshToken(
                user.getEmail(),
                jwtService.extractJti(refreshToken),
                refreshToken,
                jwtService.extractExpiration(refreshToken)
        );

        // Return just the access token
        return new AuthResponse(accessToken);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken, HttpServletResponse response) {
        jwtService.verifyToken(refreshToken);

        // Extract metadata from tokens
        String email = jwtService.extractUsername(refreshToken);
        String refreshJti = jwtService.extractJti(refreshToken);

        // Check if the user exists in the database
        User user = userRepository.findByEmailWithRoles(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with email: " + email));

        // Compare with the stored refresh token in Redis database
        if (!redisTokenService.validateRefreshToken(email,refreshJti,refreshToken)) {
            throw new IllegalArgumentException("Refresh token is invalid");
        }

        // Blacklisting the refresh token
        redisTokenService.blackListRefreshToken(refreshJti,jwtService.extractExpiration(refreshToken));

        // Revoke the old refresh token
        redisTokenService.revokeRefreshToken(email,refreshJti);

        // Generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        // Set cookies
        addCookie(response,"access_token",newAccessToken,30*60);
        addCookie(response, "refresh_token", newRefreshToken,15*24*60*60);

        // Save the new refresh token to Redis database
        redisTokenService.saveRefreshToken(
                email,
                jwtService.extractJti(newRefreshToken),
                newRefreshToken,
                jwtService.extractExpiration(newRefreshToken)
        );

        return new AuthResponse(newAccessToken);
    }

    @Override
    public void signOut(String accessToken,String refreshToken, HttpServletResponse response) {
        jwtService.verifyToken(refreshToken);

        // Extract metadata from tokens
        String email = jwtService.extractUsername(refreshToken);
        String refreshJti = jwtService.extractJti(refreshToken);

        // Validation check
        if (!redisTokenService.validateRefreshToken(email,refreshJti,refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        // Blacklisting and revoking tokens
        redisTokenService.blackListRefreshToken(refreshJti,jwtService.extractExpiration(refreshToken));
        redisTokenService.revokeRefreshToken(email,refreshJti);

        // Clear cookies
        deleteCookie(response,"access_token");
        deleteCookie(response,"refresh_token");
    }

    @Override
    public void addCookie(HttpServletResponse response, String name, String value, long maxAge) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    @Override
    public void deleteCookie(HttpServletResponse response, String name) {
        ResponseCookie cookie = ResponseCookie.from(name,"")
                .path("/")
                .maxAge(0)
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

}
