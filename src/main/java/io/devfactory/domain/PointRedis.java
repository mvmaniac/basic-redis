package io.devfactory.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@RedisHash("point")
public class PointRedis {

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
        // 저장된 데이터보다 최신 데이터일 경우
        if (refreshTime.isAfter(this.refreshTime)) {
            this.amount = amount;
            this.refreshTime = refreshTime;
        }
    }

}
