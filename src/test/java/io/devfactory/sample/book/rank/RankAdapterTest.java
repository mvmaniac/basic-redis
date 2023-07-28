package io.devfactory.sample.book.rank;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RankAdapterTest {

  @Autowired
  private RankAdapter rankAdapter;

  @DisplayName("Rank 테스트")
  @Test
  void simulate() {
    final var firstUserId = 10L;
    final var secondUserId = 20L;
    final var thirdUserId = 30L;
    final var fourthUserId = 40L;
    final var fifthUserId = 50L;
    final var eventId = 1000L;

    rankAdapter.clear(eventId);

    rankAdapter.addRank(eventId, firstUserId, 100d);
    rankAdapter.addRank(eventId, secondUserId, 110d);
    rankAdapter.addRank(eventId, thirdUserId, 120d);
    rankAdapter.addRank(eventId, fourthUserId, 130d);
    rankAdapter.addRank(eventId, fifthUserId, 140d);

    rankAdapter.addRank(eventId, secondUserId, 150d);
    rankAdapter.addRank(eventId, firstUserId, 200d);

    List<Long> topRankers = rankAdapter.getTopRankers(eventId, 3);

    assertThat(topRankers.get(0)).isEqualTo(firstUserId);
    assertThat(topRankers.get(1)).isEqualTo(secondUserId);
    assertThat(topRankers.get(2)).isEqualTo(fifthUserId);

    assertThat(rankAdapter.getRankAmount(eventId, firstUserId)).isEqualTo(200);
    assertThat(rankAdapter.getRankAmount(eventId, secondUserId)).isEqualTo(150);
    assertThat(rankAdapter.getRankAmount(eventId, fifthUserId)).isEqualTo(140);
  }

}
