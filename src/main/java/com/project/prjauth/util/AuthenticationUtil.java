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

    public static String getCurrentUserEmail() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        if (principal instanceof UserDetails ud) {
            return ud.getUsername();
        }
        throw new IllegalStateException("Cannot extract principal");
    }

    public static Long getUserId() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((CustomUserDetails) principal).getId();
        }
        throw new IllegalStateException("Cannot extract user id from authentication object");
    }

    public static User getUser() {
        Authentication authentication = getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        throw new IllegalStateException("Cannot extract user from authentication object");
    }

    /**
     * Extract principal (contains email) from different types of
     * an authentication object (from many cases)
     */
    public static String extractPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUsername();
        }
        if (principal instanceof String) {
            return (String) principal;
        }
        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        // Throw exception if different a type of principal is found
        throw new IllegalStateException("Unsupported principal type: " + principal);
    }
}
