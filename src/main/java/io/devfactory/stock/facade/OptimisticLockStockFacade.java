package io.devfactory.stock.facade;

import io.devfactory.stock.service.OptimisticLockStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class OptimisticLockStockFacade {

  private final OptimisticLockStockService optimisticLockStockService;

  public void decrease(Long id, Long quantity) throws InterruptedException {
    // OptimisticLock은 실패 했을 때 재시도를 해주어야 함
    while (true) {
      try {
        optimisticLockStockService.decrease(id, quantity);
        break;
      } catch (Exception e) {
        TimeUnit.MILLISECONDS.sleep(50);
      }
    }
  }

}
