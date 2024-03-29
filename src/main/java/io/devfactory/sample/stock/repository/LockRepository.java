package io.devfactory.sample.stock.repository;

import io.devfactory.sample.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// 실무에서는 다른 데이터소스를 사용(Connection Pool 문제) 및 JPA가 아닌 JDBC를 사용
@SuppressWarnings({"SqlDialectInspection", "SqlNoDataSourceInspection"})
public interface LockRepository extends JpaRepository<Stock, Long> {

  @Query(value = "select get_lock(:key, 3000)")
  void getLock(String key);

  @Query(value = "select release_lock(:key)")
  void releaseLock(String key);

}
