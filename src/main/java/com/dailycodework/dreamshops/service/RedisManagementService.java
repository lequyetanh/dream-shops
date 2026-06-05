package com.dailycodework.dreamshops.service;

import jakarta.transaction.Transactional;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class RedisManagementService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, Object> hashOperations;

    public RedisManagementService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    // lưu key-value vào redis, tự động hết hạn sau 1 khoảng thời gian
    public void setValueWithExpirationSeconds(String key, Object value, Duration expirationSeconds) {
        try {
            // Convert value to String để tránh serialization issues
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue, expirationSeconds);
            // log.debug("✅ Redis SET: key={}, ttl={}, value_length={}", key, expirationSeconds.getSeconds(), stringValue.length());

            // Verify key was set
            Object verify = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setValueWithTimeUnit(String key, Object value, long timeout, TimeUnit unit) {
        try {
            // Convert value to String để tránh serialization issues
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue, timeout, unit);
            long ttlSeconds = unit.toSeconds(timeout);
            // log.info("✅ Redis SET: key={}, ttl={}s, value_length={}", key, ttlSeconds, stringValue.length());

            // Verify key was set
            Object verify = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            throw e;
        }
    }

    public void setValue(String key, Object value) {
        try {
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(key, stringValue);
            // log.debug("✅ Redis SET (no TTL): key={}, value_length={}", key, stringValue.length());
        } catch (Exception e) {
            throw e;
        }
    }

    public void setValue(String prefix, String key, Object value) {
        try {
            String fullKey = prefix + "-" + key;
            String stringValue = value != null ? value.toString() : "";
            redisTemplate.opsForValue().set(fullKey, stringValue);
            // log.debug("✅ Redis SET (prefixed, no TTL): key={}, value_length={}", fullKey, stringValue.length());
        } catch (Exception e) {
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

    // key don le
    public void putToHash(String key, String property, Object data) {
        hashOperations.put(key, property, data);
    }

    public Object getInHash(String key, String property) {
        return hashOperations.get(key, property);
    }

    public Long increaseHash(String key, String property, long delta) {
        return hashOperations.increment(key, property, delta);
    }
}
