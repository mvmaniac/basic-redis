package io.devfactory.sample.book.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.devfactory.config.RedisCacheKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class CacheService {

    private final ObjectMapper objectMapper;

    // CacheManager가 여러개 있는 경우 명시 (cacheManager = "bookCacheManager") or CacheResolver를 구현하여 처리
    // 캐시 조회 only, null 값인 경우 cache 하지 않음
    // unless는 메서드를 호출한 뒤에 평가
    @Cacheable(value = RedisCacheKey.BOOK_CACHE, unless = "null == #result")
    public String getCacheById(Long id) {
        log.debug("[dev] getCacheById no cache...{}", id);
        return null;
    }

    @Cacheable(value = RedisCacheKey.BOOK_CACHE_JSON, unless = "null == #result")
    public CacheResponse getCacheJsonById(Long id) {
        log.debug("[dev] getCacheJsonById no cache...{}", id);
        return null;
    }

    // 캐시 등록 및 수정 only
    // condition은 메서드를 호출하기 전에 평가
    @CachePut(value = RedisCacheKey.BOOK_CACHE, key = "#request.id", condition = "#request.name > '' && #request.address.length() > 10")
    public String postCache(CacheRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(new CacheResponse(request.getId(), request.getName(), request.getAddress(), request.getDate()));
    }

    @CachePut(value = RedisCacheKey.BOOK_CACHE_JSON, key = "#request.id", condition = "#request.name > '' && #request.address.length() > 10")
    public CacheResponse postCacheJson(CacheRequest request) {
        return new CacheResponse(request.getId(), request.getName(), request.getAddress(), request.getDate());
    }

    // 캐시 삭제 only
    @CacheEvict(value = RedisCacheKey.BOOK_CACHE)
    public void deleteCacheById(Long id) {
        // deleting cache
    }

    // 캐시 등록 및 조회
    @Cacheable(value = RedisCacheKey.BOOK_CACHE_JSON, keyGenerator = "cacheKeyGenerator")
    public CacheResponse getAndPostCache(CacheRequest request) {
        log.debug("[dev] getAndPostCache cached...{}", request);
        return new CacheResponse(request.getId(), request.getName(), request.getAddress(), LocalDateTime.now());
    }

}
