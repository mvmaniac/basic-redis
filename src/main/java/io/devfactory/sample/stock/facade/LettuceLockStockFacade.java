package io.devfactory.sample.stock.facade;

import io.devfactory.sample.stock.repository.RedisLockRepository;
import io.devfactory.sample.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class LettuceLockStockFacade {

  private final StockService stockService;
  private final RedisLockRepository redisLockRepository;

  public void decrease(Long id, Long quantity) throws InterruptedException {
    while (!redisLockRepository.lock(id)) {
      TimeUnit.MILLISECONDS.sleep(100);
    }

    try {
      stockService.decrease(id, quantity);
    } catch (Exception e) {
      redisLockRepository.unlock(id);
    }
  }

}
