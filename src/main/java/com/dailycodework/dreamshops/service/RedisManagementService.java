package com.dailycodework.dreamshops.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(RedisManagementService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisManagementService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValueWithExpirationSeconds(String key, Object value, Duration expirationSeconds) {
        redisTemplate.opsForValue().set(key, value, expirationSeconds);
    }

    public void setValueWithTimeUnit(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValue(String prefix, String key, Object value) {
        redisTemplate.opsForValue().set(prefix + "-" + key, value);
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
        redisTemplate.opsForSet().add(key, value);
        redisTemplate.expire(key, time, timeUnit);
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

    public void addToSortedSet(String key, Object member, double score, long time, TimeUnit timeUnit) {
        redisTemplate.opsForZSet().add(key, member, score);
        redisTemplate.expire(key, time, timeUnit);
    }

    public void removeFromSortedSetByScore(String key, double minScore, double maxScore) {
        redisTemplate.opsForZSet().removeRangeByScore(key, minScore, maxScore);
    }

    public Long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }
}
