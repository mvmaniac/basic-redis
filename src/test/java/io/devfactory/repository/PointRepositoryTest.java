package io.devfactory.repository;

import io.devfactory.domain.Point;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PointRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Test
    public void 등록_및_조회를_할_수_있다() {
        // given
        Point savedPoint = pointRepository.save(new Point(1000L, LocalDateTime.of(2019, 9, 24, 20, 0)));

        // when
        Point findPoint = pointRepository.findById(savedPoint.getId()).orElse(Point.builder().build());

        // then
        assertThat(findPoint.getId()).isEqualTo(savedPoint.getId());
        assertThat(findPoint.getAmount()).isEqualTo(savedPoint.getAmount());
        assertThat(findPoint.getRefreshTime()).isEqualTo(savedPoint.getRefreshTime());
    }

}