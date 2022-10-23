package io.devfactory.stock.service;

import io.devfactory.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class StockService {

  private final StockRepository stockRepository;

  public void decrease(Long id, Long quantity) {
    // 재고 가져오기
    final var foundStock = stockRepository.findById(id).orElseThrow();

    // 재고 감소
    foundStock.decrease(quantity);

    // 저장
    stockRepository.save(foundStock);
  }

}
