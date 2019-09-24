package io.devfactory.repository;

import io.devfactory.domain.Point;
import io.devfactory.domain.PointRedis;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PointRedisRepositoryTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PointRedisRepository pointRedisRepository;

    @After
    public void tearDown() {
        pointRedisRepository.deleteAll();
    }

    @Test
    public void 직접_등록_및_조회를_할_수_있다() {
        String key = "random";
        String value = RandomStringUtils.randomAlphabetic(5, 10);

        ValueOperations<String, String> vop = redisTemplate.opsForValue();
        vop.set(key, value);

        String getValue = vop.get(key);

        log.debug("random value is {}", getValue);
        assertThat(getValue).isEqualTo(value);
    }

    @Test
    public void 등록_및_조회를_할_수_있다() {
        // given
        PointRedis savedPointRedis = pointRedisRepository.save(new PointRedis(1000L, LocalDateTime.of(2019, 9, 24, 20, 0)));

        // when
        PointRedis findPointRedis = pointRedisRepository.findById(savedPointRedis.getId()).orElse(PointRedis.builder().build());

        // then
        assertThat(findPointRedis.getId()).isEqualTo(savedPointRedis.getId());
        assertThat(findPointRedis.getAmount()).isEqualTo(savedPointRedis.getAmount());
        assertThat(findPointRedis.getRefreshTime()).isEqualTo(savedPointRedis.getRefreshTime());
    }

}