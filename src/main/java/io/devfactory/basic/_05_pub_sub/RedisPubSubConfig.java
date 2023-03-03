package io.devfactory.basic._05_pub_sub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

@Configuration(proxyBeanMethods = false)
public class RedisPubSubConfig {

  // RedisRepositoryConfig에 등록되어 있음
  private final RedisConnectionFactory redisConnectionFactory;

  public RedisPubSubConfig(RedisConnectionFactory redisConnectionFactory) {
    this.redisConnectionFactory = redisConnectionFactory;
  }

  @Bean
  public RedisMessageListenerContainer redisContainer() {
    final var container = new RedisMessageListenerContainer();
    container.setConnectionFactory(redisConnectionFactory);
    return container;
  }

}
