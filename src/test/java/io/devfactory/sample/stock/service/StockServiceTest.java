package io.devfactory.sample.stock.service;

import io.devfactory.sample.stock.domain.Stock;
import io.devfactory.sample.stock.repository.StockRepository;
import io.devfactory.sample.stock.service.StockService;
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
class StockServiceTest {

  @Autowired
  private StockService stockService;

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

  @DisplayName("단일 재고 감소 테스트")
  @Test
  void stock_decrease_one() {
    stockService.decrease(1L, 1L);

    final var foundStock = stockRepository.findById(1L).orElseThrow();
    assertThat(foundStock.getQuantity()).isEqualTo(99);
  }

  @DisplayName("다중 재고 감소 테스트 - 레이스 컨디션 발생")
  @Test
  void stock_decrease_multiple_error() throws Exception {
    int threadCount = 100;

    final var executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    final var countDownLatch = new CountDownLatch(threadCount);

    for (int i = 0; i < threadCount; i += 1) {
      executorService.submit(() -> {
        try {
          stockService.decrease(1L, 1L);
        } finally {
          countDownLatch.countDown();
        }
      });
    }

    countDownLatch.await();

    final var foundStock = stockRepository.findById(1L).orElseThrow();
    assertThat(foundStock.getQuantity()).isZero();
  }

}
