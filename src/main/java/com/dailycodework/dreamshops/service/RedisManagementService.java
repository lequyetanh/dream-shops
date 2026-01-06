package com.dailycodework.dreamshops.service;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisManagementService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisManagementService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValueWithExpirationSeconds(String key, Object value, Duration expirationSeconds) {
        try {
            // Convert value to String để tránh serialization issues
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue, expirationSeconds);
//            log.debug("✅ Redis SET: key={}, ttl={}, value_length={}", key, expirationSeconds.getSeconds(), stringValue.length());

            // Verify key was set
            Object verify = redisTemplate.opsForValue().get(key);
            if (verify == null) {
//                log.error("❌ Redis SET FAILED - key not found after set: key={}", key);
            }
        } catch (Exception e) {
//            log.error("❌ Redis SET ERROR: key={}, error={}", key, e.getMessage(), e);
            throw e;
        }
    }

    public void setValueWithTimeUnit(String key, Object value, long timeout, TimeUnit unit) {
        try {
            // Convert value to String để tránh serialization issues
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue, timeout, unit);
            long ttlSeconds = unit.toSeconds(timeout);
//            log.info("✅ Redis SET: key={}, ttl={}s, value_length={}", key, ttlSeconds, stringValue.length());

            // Verify key was set
            Object verify = redisTemplate.opsForValue().get(key);
            if (verify == null) {
//                log.error("❌ Redis SET FAILED - key not found after set: key={}", key);
            } else {
//                log.debug(
//                        "✅ Redis VERIFY OK: key={}, value={}",
//                        key,
//                        verify.toString().substring(0, Math.min(50, verify.toString().length()))
//                );
            }
        } catch (Exception e) {
//            log.error("❌ Redis SET ERROR: key={}, error={}", key, e.getMessage(), e);
            throw e;
        }
    }

    public void setValue(String key, Object value) {
        try {
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue);
//            log.debug("✅ Redis SET (no TTL): key={}, value_length={}", key, stringValue.length());
        } catch (Exception e) {
//            log.error("❌ Redis SET ERROR: key={}, error={}", key, e.getMessage(), e);
            throw e;
        }
    }

    public void setValue(String prefix, String key, Object value) {
        try {
            String fullKey = prefix + "-" + key;
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(fullKey, stringValue);
//            log.debug("✅ Redis SET (prefixed, no TTL): key={}, value_length={}", fullKey, stringValue.length());
        } catch (Exception e) {
//            log.error("❌ Redis SET ERROR: key={}, error={}", prefix + "-" + key, e.getMessage(), e);
            throw e;
        }
    }

    public Object getValue(String prefix, String key) {
        return redisTemplate.opsForValue().get(prefix + "-" + key);
    }

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public void pushToSet(String key, long time, TimeUnit timeUnit, Object... value) {
        try {
            // Convert values to String array
            String[] stringValues = new String[value.length];
            for (int i = 0; i < value.length; i++) {
                stringValues[i] = value[i] != null ? value[i].toString() : "";
            }

            Long addedCount = redisTemplate.opsForSet().add(key, (Object[]) stringValues);

            Long currentTTL = redisTemplate.getExpire(key, TimeUnit.SECONDS);
            if (currentTTL == null || currentTTL == -1) {
                Boolean expireResult = redisTemplate.expire(key, time, timeUnit);
                long ttlSeconds = timeUnit.toSeconds(time);
//                log.info(
//                        "✅ Redis SADD: key={}, added={}, ttl={}s (NEW KEY), expire_success={}",
//                        key,
//                        addedCount,
//                        ttlSeconds,
//                        expireResult
//                );
            } else {
//                log.info("✅ Redis SADD: key={}, added={}, ttl={}s (EXISTING, TTL NOT RESET)", key, addedCount, currentTTL);
            }

            // Verify set size
            Long setSize = redisTemplate.opsForSet().size(key);
//            log.debug("✅ Redis SET SIZE after SADD: key={}, size={}", key, setSize);
        } catch (Exception e) {
//            log.error("❌ Redis SADD ERROR: key={}, error={}", key, e.getMessage(), e);
            throw e;
        }
    }

    public Set<Object> getByKey(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Long getSizeSet(String key) {
        return redisTemplate.opsForSet().size(key);
    }

    public void removeInSet(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    public void deleteMultiKeys(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public List<String> getKeysStartingWith(String prefix, Integer maxCountResult) {
        List<String> keys = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(maxCountResult).build();

        redisTemplate.execute(
                (RedisCallback<Void>) connection -> {
                    try (var cursor = connection.scan(options)) {
                        cursor.forEachRemaining(key -> keys.add(new String(key)));
                    }
                    return null;
                }
        );

        return keys;
    }

    /**
     * Scan keys by pattern using Redis SCAN command
     *
     * @param pattern Pattern to match (e.g., "otp:pending:callback:*")
     * @return Set of matching keys
     */
    public Set<String> scanKeys(String pattern) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
        Set<String> keys = new java.util.HashSet<>();

        redisTemplate.execute(
                (RedisCallback<Void>) connection -> {
                    try (var cursor = connection.scan(options)) {
                        cursor.forEachRemaining(key -> keys.add(new String(key)));
                    }
                    return null;
                }
        );

        return keys;
    }

    public void deleteByPrefix(String prefix) {
        Set<String> keys = redisTemplate.keys(prefix + "*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
