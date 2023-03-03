package io.devfactory.sample.point.repository;

import io.devfactory.sample.point.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

}
