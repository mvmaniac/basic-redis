package io.devfactory.sample.point.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.devfactory.sample.point.domain.PointRedis;
import java.time.LocalDateTime;

import io.devfactory.sample.point.repository.PointRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Slf4j
@SpringBootTest
class PointRedisRepositoryTest {

  @Autowired
  private RedisTemplate<String, String> redisTemplate;

  @Autowired
  private PointRedisRepository pointRedisRepository;

  @AfterEach
  void tearDown() {
    pointRedisRepository.deleteAll();
  }

  @DisplayName("직접 등록 및 조회를 할 수 있다")
  @Test
  void 직접_등록_및_조회를_할_수_있다() {
    String key = "random";
    String value = RandomStringUtils.randomAlphabetic(5, 10);

    ValueOperations<String, String> vop = redisTemplate.opsForValue();
    vop.set(key, value);

    String getValue = vop.get(key);

    log.debug("random value is {}", getValue);
    assertThat(getValue).isEqualTo(value);
  }

  @DisplayName("등록 및 조회를 할 수 있다")
  @Test
  void 등록_및_조회를_할_수_있다() {
    // given
    PointRedis savedPointRedis = pointRedisRepository
        .save(new PointRedis(1000L, LocalDateTime.of(2019, 9, 24, 20, 0)));

    // when
    PointRedis findPointRedis = pointRedisRepository.findById(savedPointRedis.getId())
        .orElse(PointRedis.builder().build());

    // then
    assertThat(findPointRedis.getId()).isEqualTo(savedPointRedis.getId());
    assertThat(findPointRedis.getAmount()).isEqualTo(savedPointRedis.getAmount());
    assertThat(findPointRedis.getRefreshTime()).isEqualTo(savedPointRedis.getRefreshTime());
  }

}
