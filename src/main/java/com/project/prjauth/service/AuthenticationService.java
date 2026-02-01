package com.project.prjauth.service;

import com.project.prjauth.dto.request.LoginRequest;
import com.project.prjauth.dto.response.AuthResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {
    UserDetails authenticate(String username, String password);
    AuthResponse signIn(LoginRequest loginRequest, HttpServletResponse response);
    AuthResponse refreshToken(String refreshToken, HttpServletResponse response);
    void signOut(String accessToken,String refreshToken, HttpServletResponse response);
    void addCookie(HttpServletResponse response, String name, String value, long maxAge);
    void deleteCookie(HttpServletResponse response, String name);
}
