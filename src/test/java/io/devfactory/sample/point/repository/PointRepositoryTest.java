package io.devfactory.sample.point.repository;

import static org.assertj.core.api.Assertions.assertThat;

import io.devfactory.sample.point.domain.Point;
import java.time.LocalDateTime;

import io.devfactory.sample.point.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PointRepositoryTest {

  @Autowired
  private PointRepository pointRepository;

  @DisplayName("등록 및 조회를 할 수 있다")
  @Test
  void 등록_및_조회를_할_수_있다() {
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
