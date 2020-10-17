package io.devfactory.repository;

import io.devfactory.client.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {

}
