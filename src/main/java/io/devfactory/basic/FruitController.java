package io.devfactory.basic;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings("squid:S101")
@RequestMapping("/fruit")
@RestController
public class FruitController {

  private final StringRedisTemplate redisTemplate;

  public FruitController(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @GetMapping("/set")
  public String setFruit(@RequestParam String name) {
    final var operations = redisTemplate.opsForValue();
    operations.set("fruit", name);
    return "saved";
  }

  @GetMapping("/get")
  public String getFruit() {
    return redisTemplate.opsForValue().get("fruit");
  }

}
