package com.jaewon.wolboo.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RedisLockService {

    private static final Logger logger = LoggerFactory.getLogger(RedisLockService.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean acquireLock(String key, String value, long expirationSeconds) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, value, Duration.ofSeconds(expirationSeconds));
        logger.info("Trying to acquire lock: key={}, success={}", key, success);
        return success != null && success;
    }

    public void releaseLock(String key, String value) {
        String currentValue = (String) redisTemplate.opsForValue().get(key);
        if (value.equals(currentValue)) {
            redisTemplate.delete(key);
            logger.info("Released lock: key={}", key);
        } else {
            logger.warn("Lock release failed (mismatched value): key={}", key);
        }
    }
}
