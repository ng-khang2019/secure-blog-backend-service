package com.project.prjauth.service.Impl;

import com.project.prjauth.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.apache.commons.codec.digest.DigestUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {
    private final RedisTemplate<String,Object> redisTemplate;

    @Override
    public String hashToken(String token) {
        return DigestUtils.sha256Hex(token);
    }

    @Override
    public void saveRefreshToken(String email, String jti, String refreshToken, Instant expiration) {
        String key = "refresh:" + email + ":" + jti;
        long ttl = Duration.between(Instant.now(),expiration).getSeconds();
        redisTemplate.opsForValue()
                .set(key, hashToken(refreshToken), ttl, TimeUnit.SECONDS);
    }

    @Override
    public void blackListRefreshToken(String jti, Instant expiration) {
        long ttl = Duration.between(Instant.now(),expiration).getSeconds();
        // Do nothing if the token has expired
        if (ttl <= 0) return;

        // Set true means it's blacklisted
        redisTemplate.opsForValue()
                .set("blacklist:refresh:" + jti,true,ttl, TimeUnit.SECONDS);

    }

    @Override
    public void revokeRefreshToken(String email, String jti) {
        redisTemplate.delete("refresh:" + email + ":" + jti);
    }

    @Override
    public boolean validateRefreshToken(String email, String jti, String refreshToken) {
        validateNotBlackListed(jti);
        String key = "refresh:" + email + ":" + jti;
        Object storedRefreshToken = redisTemplate.opsForValue().get(key);
        if (storedRefreshToken == null) return false;
        return storedRefreshToken.equals(hashToken(refreshToken));
    }

    @Override
    public void validateNotBlackListed(String jti) {
        if (redisTemplate.hasKey("blacklist:refresh:" + jti)) {
            throw new IllegalArgumentException("Refresh token is blacklisted");
        }
    }
}
