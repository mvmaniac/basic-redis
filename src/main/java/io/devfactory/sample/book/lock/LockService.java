package io.devfactory.sample.book.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LockService {

  private final LockAdapter lockAdapter;

  @Transactional(timeout = 10)
  public boolean attachEvent(Long id, Long userId) {
    // false로 반환된다면 다른 인스턴스나 스레도로 락이 생성되었음을 의미하므로 실패로 처리하고 false로 반환함
    // true이거나 null인 경우 false 반환
    if (!lockAdapter.holdLock(id, userId)) {
      log.debug("[dev] lock fail...{}, {}", id, userId);
      return false;
    }

    // lock이 걸려서 통과된다면 DB조회 및 수정같은 로직 수행
    log.debug("[dev] lock success...{}, {}", id, userId);
    return true;
  }

}
