package io.devfactory.sample.book.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Arrays;

public class CacheKeyGenerator implements KeyGenerator {

  private static final String PREFIX = "CACHE::";

  @NonNull
  @Override
  public Object generate(@NonNull Object target, @NonNull Method method,
      @Nullable Object... params) {

    if (null == params) {
      return "NULL";
    }

    return Arrays.stream(params)
        .filter(CacheRequest.class::isInstance)
        .findFirst()
        .map(CacheRequest.class::cast)
        .map(cacheRequest -> PREFIX + cacheRequest.getId())
        .orElse(SimpleKeyGenerator.generateKey(params).toString());
  }

}
