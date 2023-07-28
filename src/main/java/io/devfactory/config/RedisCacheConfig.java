package io.devfactory.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisCacheConfig {

    @Bean
    public RedisCacheManager defaultCacheManager(RedisConnectionFactory connectionFactory) {
        final var defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(10)) // 기본 TTL (Time To Live)
                .computePrefixWith(CacheKeyPrefix.simple())
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

        final var configMap = new HashMap<String, RedisCacheConfiguration>();
        configMap.put(RedisCacheKey.USER_AGE, defaultCacheConfig.entryTtl(Duration.ofSeconds(10)));  // 특정 캐시에 대한 TTL

        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(connectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(configMap)
                .build();
    }

}
