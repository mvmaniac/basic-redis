package io.devfactory.config;

import io.devfactory.sample.book.cache.MultipleCacheResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@EnableCaching
@Configuration(proxyBeanMethods = false)
public class RedisCachingConfig implements CachingConfigurer {

    // 리스트로 주입을 받기 위해 별도의 config 클래스로 만듬, 순환참조 방지
    private final List<CacheManager> cacheManagers;

    @Bean
    @Override
    public CacheResolver cacheResolver() {
        return new MultipleCacheResolver(cacheManagers);
    }

}
