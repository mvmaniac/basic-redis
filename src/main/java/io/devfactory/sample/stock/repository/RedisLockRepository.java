package io.devfactory.sample.stock.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@RequiredArgsConstructor
@Component
public class RedisLockRepository {

  private final RedisTemplate<String, String> redisTemplate;

  public boolean lock(Long key) {
    return Boolean.TRUE.equals(
        redisTemplate.opsForValue()
            .setIfAbsent(generateKey(key), "lock", Duration.ofMillis(3_800))
    );
  }

  public boolean unlock(Long key) {
    return Boolean.TRUE.equals(redisTemplate.delete(generateKey(key)));
  }

  private String generateKey(Long key) {
    return key.toString();
  }

}
