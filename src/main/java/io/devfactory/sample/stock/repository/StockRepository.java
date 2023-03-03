package io.devfactory.sample.stock.repository;

import io.devfactory.sample.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import static jakarta.persistence.LockModeType.OPTIMISTIC;
import static jakarta.persistence.LockModeType.PESSIMISTIC_WRITE;

public interface StockRepository extends JpaRepository<Stock, Long> {

  @Lock(value = PESSIMISTIC_WRITE)
  @Query("select s from Stock s where s.id = :id")
  Stock findByIdWithPessimisticLock(Long id);

  @Lock(value = OPTIMISTIC)
  @Query("select s from Stock s where s.id = :id")
  Stock findByIdWithOptimisticLock(Long id);

}
