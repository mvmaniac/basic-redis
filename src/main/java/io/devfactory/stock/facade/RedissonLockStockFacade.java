package io.devfactory.stock.facade;

import io.devfactory.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedissonLockStockFacade {

  private final StockService stockService;
  private final RedissonClient redissonClient;

  public void decrease(Long id, Long quantity) {
    final var lock = redissonClient.getLock(id.toString());

    try {
      final var available = lock.tryLock(5, 1, TimeUnit.SECONDS);

      if (!available) {
        throw new IllegalStateException("lock 해제 실패");
      }

      stockService.decrease(id, quantity);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("InterruptedException 발생...");
    } finally {
      lock.unlock();
    }
  }

}
