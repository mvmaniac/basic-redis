package io.devfactory.sample.book.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@Component
public class LockAdapter {

  private final RedisTemplate<LockKey, Long> lockRedisTemplate;
  private final ValueOperations<LockKey, Long> operations;

  public LockAdapter(RedisTemplate<LockKey, Long> lockRedisTemplate) {
    this.lockRedisTemplate = lockRedisTemplate;
    this.operations = this.lockRedisTemplate.opsForValue();
  }

  public boolean holdLock(Long id, Long userId) {
    LockKey lockKey = LockKey.of(id);

    // 레디스에 키와 매핑되는 값이 없을 때만 데이터 생성
    // 데이터가 없어서 데이터를 생성하면 true, 데이터를 생성하지 못하면 false
    Boolean result = operations.setIfAbsent(lockKey, userId, Duration.ofSeconds(10));
    return Boolean.parseBoolean(Objects.toString(result));
  }

  public Long checkLock(Long id) {
    LockKey lockKey = LockKey.of(id);
    return operations.get(lockKey);
  }

  public void clearLock(Long id) {
    lockRedisTemplate.delete(LockKey.of(id));
  }

}
