package io.devfactory.repository;

import io.devfactory.client.PointRedis;
import org.springframework.data.repository.CrudRepository;

public interface PointRedisRepository extends CrudRepository<PointRedis, Long> {

}
