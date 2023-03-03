package io.devfactory.basic._04_leaderboard;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/leaderboard")
@RestController
public class LeaderboardController {

  private final LeaderboardService leaderboardService;

  public LeaderboardController(LeaderboardService leaderboardService) {
    this.leaderboardService = leaderboardService;
  }

  @GetMapping("/rank/{userId}")
  public Long getUserRanking(@PathVariable String userId) {
    return leaderboardService.getUserRanking(userId);
  }

  @GetMapping("/top-ranks/{limit}")
  public List<String> getTopRanks(@PathVariable int limit) {
    return leaderboardService.getTopRanks(limit);
  }

  @PostMapping("/add")
  public Boolean addUser(@RequestBody Leaderboard leaderboard) {
    return leaderboardService.addUser(leaderboard);
  }

}
