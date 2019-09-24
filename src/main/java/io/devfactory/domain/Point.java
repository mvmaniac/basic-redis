package io.devfactory.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity
public class Point {

    @Id
    @GeneratedValue
    private Long id;
    private Long amount;
    private LocalDateTime refreshTime;

    @Builder
    public Point(Long amount, LocalDateTime refreshTime) {
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
