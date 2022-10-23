package io.devfactory.stock.service;

import io.devfactory.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@RequiredArgsConstructor
@Service
public class NamedLockStockService {

  private final StockRepository stockRepository;

  // 부모의 트랜잭션과 별도로 실행
  @Transactional(propagation = REQUIRES_NEW)
  public void decrease(Long id, Long quantity) {
    // 재고 가져오기
    final var foundStock = stockRepository.findById(id).orElseThrow();

    // 재고 감소
    foundStock.decrease(quantity);

    // 저장
    stockRepository.save(foundStock);
  }

}
