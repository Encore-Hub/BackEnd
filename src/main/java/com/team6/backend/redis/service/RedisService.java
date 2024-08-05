package com.team6.backend.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String email, String refreshToken, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(email, refreshToken, duration);
    }

    public String getValues(String email) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(email);
    }

    public void deleteValues(String email) {
        redisTemplate.delete(email);
    }

    public boolean hasKey(String email) {
        return Optional.ofNullable(redisTemplate.hasKey(email))
                .orElseThrow(() -> new NullPointerException("Key not found: " + email));
    }
}
