package com.project.prjauth.service;

import java.time.Instant;

public interface RedisTokenService {
    String hashToken(String token);
    void saveRefreshToken(String email, String jti, String refreshToken, Instant expiration);
    void blackListAccessToken(String jti, Instant expiration);
    void revokeRefreshToken(String email, String jti);
    boolean validateRefreshToken(String email, String jti, String refreshToken);
    void validateNotBlackListed(String jti);
}
