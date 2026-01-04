package com.dailycodework.dreamshops.config;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import vn.softdreams.easypos.config.properties.RedisTopicProperties;

@Configuration
@EnableConfigurationProperties(RedisTopicProperties.class)
public class RedisConfig {

    private final RedisTopicProperties redisTopicProperties;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.redis.database}")
    private int redisDatabase;

    @Value("${spring.redis.password}")
    private String redisPassword;

    public RedisConfig(RedisTopicProperties redisTopicProperties) {
        this.redisTopicProperties = redisTopicProperties;
    }

    @Bean
    @Primary
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        configuration.setDatabase(redisDatabase);
        if (!Strings.isNullOrEmpty(redisPassword)) {
            configuration.setPassword(redisPassword);
        }
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public ChannelTopic updateSecurityConfigTopic() {
        return ChannelTopic.of(redisTopicProperties.getUpdateSecurityConfig());
    }

    @Bean
    public ChannelTopic updateRateLimitConfigTopic() {
        return ChannelTopic.of(redisTopicProperties.getUpdateRateLimitConfig());
    }

    @Bean
    public ChannelTopic updateRateLimitExcludedConfigTopic() {
        return ChannelTopic.of(redisTopicProperties.getUpdateRateLimitExcludedConfig());
    }

    public RedisTemplate<String, String> createTempRedisTemplate(int dbIndex) {
        // Reuse host, port, password from main factory
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setPassword(redisPassword);
        config.setDatabase(dbIndex);
        // Create a separate factory for temporary connections
        return getStringStringRedisTemplate(config);
    }

    private RedisTemplate<String, String> getStringStringRedisTemplate(RedisStandaloneConfiguration config) {
        LettuceConnectionFactory tempFactory = new LettuceConnectionFactory(config);
        tempFactory.afterPropertiesSet();
        // Create a separate template (does not affect the main RedisTemplate)
        RedisTemplate<String, String> tempTemplate = new RedisTemplate<>();
        tempTemplate.setConnectionFactory(tempFactory);
        tempTemplate.afterPropertiesSet();
        tempTemplate.setKeySerializer(new StringRedisSerializer());
        tempTemplate.setValueSerializer(new StringRedisSerializer());
        return tempTemplate;
    }
}
