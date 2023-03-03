package io.devfactory.basic._03_cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class UserExternalService {

  public String getUserName(String userId) {
    // 외부 서비스나 DB 호출 한다고 가정
    sleep();

    log.debug("[dev] {}", "Getting user name from other service...");

    if (userId.equals("A")) {
      return "Adam";
    }

    if (userId.equals("B")) {
      return "Bob";
    }

    return "";
  }

  @Cacheable(cacheNames = "user-age", key = "#userId")
  public int getUserAge(String userId) {
    // 외부 서비스나 DB 호출 한다고 가정
    sleep();

    log.debug("[dev] {}", "Getting user age from other service...");

    if (userId.equals("A")) {
      return 28;
    }

    if (userId.equals("B")) {
      return 32;
    }

    return 0;
  }

  private static void sleep() {
    try {
      TimeUnit.MILLISECONDS.sleep(500);
    } catch (InterruptedException e) {
      log.warn("Interrupted in getUserName()", e);
      Thread.currentThread().interrupt();
    }
  }

}
