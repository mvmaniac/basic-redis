package io.devfactory.basic._04_leaderboard;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
@Slf4j
@SpringBootTest
class LeaderboardServiceTest {

  @Autowired
  private LeaderboardService leaderboardService;

  @Autowired
  private StringRedisTemplate redisTemplate;

  private static final int MAX_COUNT = 1_000_000; // 백만건

  @DisplayName("inMemory 성능 테스트 (단순 정렬만)")
  @Test
  void inMemorySortPerformance() {
    final var dummyList = new ArrayList<Integer>();
    insertDummyData(leaderboard -> dummyList.add(leaderboard.score()));

    final var before = Instant.now();
    Collections.sort(dummyList); // nlogn

    final var elapsed = Duration.between(before, Instant.now());
    log.debug("[dev] elapsed: {} ms", elapsed.getNano() / 1000000);
  }

  @DisplayName("redis 성능 테스트")
  @Test
  void redisPerformance() {
    final var operations = redisTemplate.opsForZSet();
    final var size = Objects.requireNonNullElse(operations.size(LeaderboardService.LEADERBOARD_KEY), 0L);

    // 10보다 작다면, 더미 데이터 추가
    if (size < 10) {
      insertDummyData(leaderboard -> leaderboardService.addUser(leaderboard));
    }

    // 한번 미리 호출...최초 호출은 오래 걸릴 수 있기 때문에?
    leaderboardService.getTopRanks(1);

    // 1) Get user_100's rank
    var before = Instant.now();
    Long userRank = leaderboardService.getUserRanking("user-100");

    var elapsed = Duration.between(before, Instant.now());
    log.debug("[dev] user-100 rank: {}, elapsed: {} ms", userRank, elapsed.getNano() / 1000000);

    // 2) Get top 10 user list
    before = Instant.now();
    final var ranks = leaderboardService.getTopRanks(9);
    elapsed = Duration.between(before, Instant.now());

    log.debug("[dev] top ranks: {}, elapsed: {} ms", ranks, elapsed.getNano() / 1000000);
  }

  void insertDummyData(Consumer<Leaderboard> consumer) {
    for (int i = 0; i < MAX_COUNT; i += 1) {
      int score = ThreadLocalRandom.current().nextInt(MAX_COUNT); // 0 ~ 999999
      consumer.accept(new Leaderboard("user-" + i, score));
    }
  }

}
