package io.devfactory.stock.service;

import io.devfactory.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PessimisticLockStockService {

  private final StockRepository stockRepository;

  @Transactional
  public void decrease(Long id, Long quantity) {
    // 재고 가져오기
    final var foundStock = stockRepository.findByIdWithPessimisticLock(id);

    // 재고 감소
    foundStock.decrease(quantity);

    // 저장
    stockRepository.save(foundStock);
  }

}
