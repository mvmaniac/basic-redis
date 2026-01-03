package io.devfactory.basic._05_pub_sub;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Slf4j
@Service
public class ChatService implements MessageListener {

  private final RedisMessageListenerContainer container;
  private final StringRedisTemplate redisTemplate;

  public ChatService(RedisMessageListenerContainer container, StringRedisTemplate redisTemplate) {
    this.container = container;
    this.redisTemplate = redisTemplate;
  }

  public void enterCharRoom(String chatRoomName) {
    container.addMessageListener(this, new ChannelTopic(chatRoomName));

    final var in = new Scanner(System.in);
    while (in.hasNextLine()) {
      String line = in.nextLine();

      if (line.equals("q")) {
        log.debug("[dev] quite...");
        break;
      }

      redisTemplate.convertAndSend(chatRoomName, line);
    }

    container.removeMessageListener(this);
  }

  @Override
  public void onMessage(@NonNull Message message, byte[] pattern) {
    log.debug("[dev] onMessage - message: {}", message);
  }

}
