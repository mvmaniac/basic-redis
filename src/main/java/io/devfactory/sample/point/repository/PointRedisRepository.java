package io.devfactory.sample.point.repository;

import io.devfactory.sample.point.domain.PointRedis;
import org.springframework.data.repository.CrudRepository;

public interface PointRedisRepository extends CrudRepository<PointRedis, Long> {

}
