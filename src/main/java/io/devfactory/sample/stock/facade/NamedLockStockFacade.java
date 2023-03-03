package io.devfactory.sample.stock.facade;

import io.devfactory.sample.stock.service.NamedLockStockService;
import io.devfactory.sample.stock.repository.LockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NamedLockStockFacade {

  private final NamedLockStockService namedLockStockService;
  private final LockRepository lockRepository;

  public void decrease(Long id, Long quantity) {
    try {
      lockRepository.getLock(id.toString());
      namedLockStockService.decrease(id, quantity);
    } finally {
      lockRepository.releaseLock(id.toString());
    }
  }

}
