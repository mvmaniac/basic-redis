package io.devfactory.stock.facade;

import io.devfactory.stock.repository.LockRepository;
import io.devfactory.stock.service.NamedLockStockService;
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
