package com.project.prjauth.service.Impl;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.project.prjauth.entity.User;
import com.project.prjauth.repository.UserRepository;
import com.project.prjauth.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String secretKey;

    @Override
    public String generateAccessToken(User user) {
        // Short TTL - 15 minutes
        return generateToken(user, 30,ChronoUnit.MINUTES);
    }

    @Override
    public String generateRefreshToken(User user) {
        // Long TLL - 30 days
        return generateToken(user,15, ChronoUnit.DAYS);
    }

    private String generateToken(User user, long amount, ChronoUnit unit) {
        try {
            // Create header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
            // Create claims
            JWTClaimsSet claims = new JWTClaimsSet.Builder()
                    .subject(user.getEmail())
                    .issuer("blog-platform-identity-service")
                    .issueTime(new Date())
                    .expirationTime(Date.from(Instant.now().plus(amount, unit)))
                    .jwtID(UUID.randomUUID().toString())
                    .claim("scope", buildScope(user))
                    .build();
            // Bind header and claims together
            SignedJWT jwt = new SignedJWT(header, claims);
            // Sign the JWT with the secret key
            jwt.sign(new MACSigner(secretKey.getBytes()));
            // Serialize the JWT to a compact String
            return jwt.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Generate token failed: " + e);
        }
    }

    @Override
    public void verifyToken(String token) {
        try {
            // Parse the JWT to get the header and claims
            SignedJWT jwt = SignedJWT.parse(token);
            // Get the claims
            JWTClaimsSet claims = jwt.getJWTClaimsSet();
            // Get expiration time from claims
            if (claims.getExpirationTime().before(new Date())) {
                throw new IllegalArgumentException("Token has expired");
            }
            // Check email existence in database
            userRepository.findByEmail(claims.getSubject())
                    .orElseThrow(() -> new RuntimeException("User not found"));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Parsing token failed: Invalid token", e);
        }
    }

    @Override
    public String extractJti(String token) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getJWTID();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Parsing token failed: Invalid token", e);
        }
    }

    @Override
    public String extractUsername(String token) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getSubject();
        } catch (ParseException e) {
            throw new IllegalArgumentException("Parsing token failed: Invalid token", e);
        }
    }

    @Override
    public Instant extractExpiration(String token) {
        try {
            return SignedJWT.parse(token)
                    .getJWTClaimsSet()
                    .getExpirationTime()
                    .toInstant(); // Convert to Instant
        } catch (ParseException e) {
            throw new IllegalArgumentException("Parsing token failed: Invalid token", e);
        }
    }

    @Override
    public String buildScope(User user) {
        StringJoiner joiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) { // CollectionUtils allows a null collection
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_" + role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission ->
                            joiner.add(permission.getName()));
                }
            });
        }
        return joiner.toString();
    }

}
