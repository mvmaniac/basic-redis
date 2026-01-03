package io.devfactory.sample.book.lock;

import lombok.extern.slf4j.Slf4j;
import org.awaitility.Durations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@Slf4j
@SpringBootTest
class LockAdapterTest {

  private final Long firstUserId = 100L;
  private final Long secondUserId = 200L;
  private final Long thirdUserId = 300L;

  @Autowired
  private LockAdapter lockAdapter;

  @DisplayName("firstUserId가 락을 선점")
  @Test
  void testLock() {
    final var hotelId = 123123123L;

    var isSuccess = lockAdapter.holdLock(hotelId, firstUserId);
    assertThat(isSuccess).isTrue();

    isSuccess = lockAdapter.holdLock(hotelId, secondUserId);
    assertThat(isSuccess).isFalse();

    isSuccess = lockAdapter.holdLock(hotelId, thirdUserId);
    assertThat(isSuccess).isFalse();

    final var holderId = lockAdapter.checkLock(hotelId);
    assertThat(holderId).isEqualTo(firstUserId);
  }

  @DisplayName("3명이 동시에 락을 선점하지만 1명만 락을 잡음")
  @Test
  void testConcurrentAccess() {
    final var hotelId = 9999999L;
    lockAdapter.clearLock(hotelId);

    CyclicBarrier cyclicBarrier = new CyclicBarrier(3);

    try (var executorService = Executors.newFixedThreadPool(3)) {
      executorService.submit(new Accessor(hotelId, firstUserId, cyclicBarrier));
      executorService.submit(new Accessor(hotelId, secondUserId, cyclicBarrier));
      executorService.submit(new Accessor(hotelId, thirdUserId, cyclicBarrier));
    }

    await().pollDelay(Durations.ONE_SECOND).until(() -> {
      final var holderId = lockAdapter.checkLock(hotelId);
      log.debug("[dev] holder: {}", holderId);

      assertThat(List.of(firstUserId, secondUserId, thirdUserId)).contains(holderId);

      lockAdapter.clearLock(hotelId);
      return true;
    });
  }

  class Accessor implements Runnable {

    private final Long hotelId;
    private final Long userId;
    private final CyclicBarrier cyclicBarrier;

    public Accessor(Long hotelId, Long userId, CyclicBarrier cyclicBarrier) {
      this.hotelId = hotelId;
      this.userId = userId;
      this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void run() {
      try {
        cyclicBarrier.await();

        log.debug("[dev] holdLock: {}, {}", hotelId, userId);

        lockAdapter.holdLock(hotelId, userId);
      } catch (InterruptedException | BrokenBarrierException e) {
        throw new RuntimeException(e);
      }
    }

  }

}
