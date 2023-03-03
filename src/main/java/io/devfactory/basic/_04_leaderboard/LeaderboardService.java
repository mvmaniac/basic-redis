package io.devfactory.basic._04_leaderboard;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class LeaderboardService {

  public static final String LEADERBOARD_KEY = "leaderboard";

  private final StringRedisTemplate redisTemplate;

  public LeaderboardService(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @PostConstruct
  public void init() {
    final var operations = redisTemplate.opsForZSet();
    operations.add(LEADERBOARD_KEY, "dummy-1", 10);
    operations.add(LEADERBOARD_KEY, "dummy-2", 20);
    operations.add(LEADERBOARD_KEY, "dummy-3", 30);
    operations.add(LEADERBOARD_KEY, "dummy-4", 40);
    operations.add(LEADERBOARD_KEY, "dummy-5", 50);
  }

  // 0부터 시작하기 때문에 +1 해줌
  public Long getUserRanking(String userId) {
    final var operations = redisTemplate.opsForZSet();
    return Objects.requireNonNullElse(operations.reverseRank(LEADERBOARD_KEY, userId), -1L) + 1;
  }

  public List<String> getTopRanks(int limit) {
    final var operations = redisTemplate.opsForZSet();
    final var rangeSet = operations.reverseRange(LEADERBOARD_KEY, 0, limit);
    return new ArrayList<>(Objects.requireNonNullElse(rangeSet, Collections.emptySet()));
  }

  public Boolean addUser(Leaderboard leaderboard) {
    final var operations = redisTemplate.opsForZSet();
    operations.add(LEADERBOARD_KEY, leaderboard.userId(), leaderboard.score());
    return true;
  }

}
