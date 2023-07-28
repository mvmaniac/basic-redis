package io.devfactory.sample.book.rank;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static java.util.Collections.emptyList;

@Slf4j
@Component
public class RankAdapter {

  private static final String PREFIX = "RANK::";

  private final RedisTemplate<String, Long> rankRedisTemplate;
  private final ZSetOperations<String, Long> operations;

  public RankAdapter(RedisTemplate<String, Long> rankRedisTemplate) {
    this.rankRedisTemplate = rankRedisTemplate;
    this.operations = this.rankRedisTemplate.opsForZSet();
  }

  public void addRank(Long id, Long userId, double score) {
    String key = this.serializeKey(id);
    operations.add(key, userId, score);
  }

  public List<Long> getTopRankers(Long id, Integer fetchCount) {
    String key = this.serializeKey(id);
    Set<Long> topRankers = operations.reverseRangeByScore(key, 0D, Double.MAX_VALUE, 0, fetchCount);
    return CollectionUtils.isEmpty(topRankers) ? emptyList() : topRankers.stream().toList();
  }

  public double getRankAmount(Long id, Long userId) {
    String key = this.serializeKey(id);
    Double score = operations.score(key, userId);
    return null == score ? 0 : score;
  }

  public void clear(Long id) {
    String key = this.serializeKey(id);
    rankRedisTemplate.delete(key);
  }

  private String serializeKey(Long id) {
    return PREFIX + id;
  }

}
