package com.project.prjauth.util;

import com.project.prjauth.entity.CustomUserDetails;
import com.project.prjauth.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

    private AuthenticationUtil() {}

    /**
     * Get authentication object from security context
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Extract principal (contains email) from different types of
     * an authentication object (from many cases)
     */
    public static String extractPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        if (principal instanceof String s) {
            return s;
        }
        // Throw exception if different a type of principal is found
        throw new IllegalStateException("Unsupported principal type: " + principal);
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();
        return extractPrincipal(authentication);
    }

    public static Long getUserId() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        throw new IllegalStateException("Cannot extract user id from authentication object");
    }
}
