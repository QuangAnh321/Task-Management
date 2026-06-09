package com.example.task_management.services;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterService {

    private static final Long TIME_WINDOW_IN_SECONDS = 61L;
    private static final DateTimeFormatter HOUR_MINUTE_FORMATTER = DateTimeFormatter.ofPattern("H'h'mm");

    private final RedisService redisService;
    private final int maxRequests;

    public RateLimiterService(RedisService redisService,
        @Value("${rate.limiter.max.requests:10}") int maxRequests) {
        this.redisService = redisService;
        this.maxRequests = maxRequests;
    }

    /**
     * Key format: "{user_email}:{hour_and_minute}" to limit requests per user per minute}"
     * Example key: "user@example.com:14h30"
     */
    public boolean isAllowed(String userEmail) {
        String redisKey = createRateLimiterKey(userEmail);
        String currentValue = redisService.getValue(redisKey);
        int currentCount = currentValue != null ? Integer.parseInt(currentValue) : 0;

        if (currentCount >= maxRequests) {
            return false; // Rate limit exceeded
        }

        // Increment the count and set expiry if it's a new key, otherwise just update the count with the same expiry
        if (currentCount == 0) {
            redisService.setValueWithExpiry(redisKey, "1", TIME_WINDOW_IN_SECONDS, TimeUnit.SECONDS);
        } else {
            redisService.setValueWithExpiry(redisKey, String.valueOf(currentCount + 1), TIME_WINDOW_IN_SECONDS, TimeUnit.SECONDS);
        }

        return true; // Allowed
    }

    private String createRateLimiterKey(String userEmail) {
        String currentHourAndMinute = LocalDateTime.now().format(HOUR_MINUTE_FORMATTER);
        return userEmail + ":" + currentHourAndMinute;
    }
}
