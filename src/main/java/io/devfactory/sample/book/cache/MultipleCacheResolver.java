package io.devfactory.sample.book.cache;

import org.jspecify.annotations.NonNull;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.*;
import java.util.stream.Collectors;

public class MultipleCacheResolver implements CacheResolver {

  private final Map<String, Cache> cacheMap = new HashMap<>();

  public MultipleCacheResolver(List<CacheManager> cacheManagers) {
    for (CacheManager cacheManager : cacheManagers) {
      cacheMap.putAll(toCachMap(cacheManager));
    }
  }

  @SuppressWarnings("DataFlowIssue")
  private Map<String, Cache> toCachMap(CacheManager cacheManager) {
    return cacheManager.getCacheNames().stream()
        .filter(name -> Objects.nonNull(cacheManager.getCache(name)))
        .collect(Collectors.toMap(name -> name, cacheManager::getCache));
  }

  @NonNull
  @Override
  public Collection<? extends Cache> resolveCaches(
      @NonNull CacheOperationInvocationContext<?> context) {
    Set<String> cacheNames = context.getOperation().getCacheNames();

    for (String cacheName : cacheNames) {
      Cache cache = cacheMap.get(cacheName);

      if (null != cache) {
        return List.of(cache);
      }
    }

    return Collections.emptyList();
  }

}
