package io.devfactory.sample.stock.facade;

import io.devfactory.sample.stock.domain.Stock;
import io.devfactory.sample.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OptimisticLockStockFacadeTest {

  @Autowired
  private OptimisticLockStockFacade optimisticLockStockFacade;

  @Autowired
  private StockRepository stockRepository;

  @BeforeEach
  public void BeforeEach() {
    final var stock = new Stock(1L, 100L);
    stockRepository.save(stock);
  }

  @AfterEach
  public void afterEach() {
    stockRepository.deleteAll();
  }

  @DisplayName("다중 재고 감소 테스트 - OptimisticLock")
  @Test
  void stock_decrease_multiple_optimistic_lock() throws Exception {
    int threadCount = 100;

    CountDownLatch countDownLatch;

    try (var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())) {
      countDownLatch = new CountDownLatch(threadCount);

      for (int i = 0; i < threadCount; i += 1) {
        executorService.submit(() -> {
          try {
            optimisticLockStockFacade.decrease(1L, 1L);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          } finally {
            countDownLatch.countDown();
          }
        });
      }
    }

    countDownLatch.await();

    final var foundStock = stockRepository.findById(1L).orElseThrow();
    assertThat(foundStock.getQuantity()).isZero();
  }

}
