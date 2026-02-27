package com.project.prjauth.util;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = AuthenticationUtil.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.of("System");
        }
        return Optional.ofNullable(AuthenticationUtil.getCurrentUserEmail());
    }
}
