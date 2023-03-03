package io.devfactory.basic._03_cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserService {

  private final UserExternalService userExternalService;

  private final StringRedisTemplate redisTemplate;

  public UserService(UserExternalService userExternalService, StringRedisTemplate redisTemplate) {
    this.userExternalService = userExternalService;
    this.redisTemplate = redisTemplate;
  }

  public UserProfile getUserProfile(String userId) {
    String userName;

    // cache-aside 방식 (laze-loading), 수동
    // 먼저 캐시 (레디스) 조회 후 있으면 캐시 사용 없으면 실제 데이터 조회 후 캐시 저장 처리
    final var operations = redisTemplate.opsForValue();
    final var cachedName = operations.get("nameKey:" + userId);

    if (cachedName != null) {
      log.debug("[dev] username cached...");
      userName = cachedName;

    } else {
      log.debug("[dev] username not cached saved...");

      userName = userExternalService.getUserName(userId);
      operations.set("nameKey:" + userId, userName, 10, TimeUnit.SECONDS); // 10초후 만료
    }

    // @Cacheable 사용, 자동
    final var userAge = userExternalService.getUserAge(userId);

    return new UserProfile(userName, userAge);
  }


}
