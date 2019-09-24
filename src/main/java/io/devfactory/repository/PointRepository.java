package io.devfactory.repository;

import io.devfactory.domain.Point;
import io.devfactory.domain.PointRedis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
