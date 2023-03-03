package io.devfactory.sample.point.domain;

import java.time.LocalDateTime;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@NoArgsConstructor
@Getter
@RedisHash("point")
public class PointRedis {

  // 만약 id 변수명을 redisId 로 한다고 하면
  // javax.persistence.Id 가 아니라 org.springframework.data.annotation.Id로 지정해야 함
  @Id
  @GeneratedValue
  private Long id;
  private Long amount;
  private LocalDateTime refreshTime;

  @Builder
  public PointRedis(Long amount, LocalDateTime refreshTime) {
    this.amount = amount;
    this.refreshTime = refreshTime;
  }

  public void refresh(long amount, LocalDateTime refreshTime) {
    // 저장된 데이터보다 최신 데이터일 경우s
    if (refreshTime.isAfter(this.refreshTime)) {
      this.amount = amount;
      this.refreshTime = refreshTime;
    }
  }

}
