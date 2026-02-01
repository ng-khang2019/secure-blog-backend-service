package com.project.prjauth.config;

import com.project.prjauth.service.Impl.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService CustomUserDetailService;
    private final JwtDecoder jwtDecoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler accessDeniedHandler;

    @Value("${frontend.url}")
    private String FRONT_URL;

    private static final String[] WHITELIST_DEV = {
            "/api/auth/**",
            "/api/public/users/register",
            "/api/public/users/{id}",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/docs/**"
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider customAuthenticationProvider = new DaoAuthenticationProvider();
        customAuthenticationProvider.setUserDetailsService(CustomUserDetailService);
        customAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return customAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults());
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(request -> request
                .requestMatchers(WHITELIST_DEV).permitAll()
                .anyRequest().authenticated());
        http.exceptionHandling(exception -> exception
                .authenticationEntryPoint(restAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler));
        http.oauth2ResourceServer(oauth2 -> oauth2
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)
                    .decoder(jwtDecoder)));
        return http.build();
    }

    @Bean
    public CorsFilter corsFilter () {
        CorsConfiguration config = new CorsConfiguration();

//        config.setAllowedOrigins(List.of(FRONT_URL));
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type","Accept-Language", "x-no-retry", "Access-Control-Allow-Origin"));
        config.setExposedHeaders(List.of("Authorization", "Content-Type", "Accept-Language"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
