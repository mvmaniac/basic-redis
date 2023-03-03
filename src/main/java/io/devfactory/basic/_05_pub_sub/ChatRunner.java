package io.devfactory.basic._05_pub_sub;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

// 테스트 시 주석 해제
// @Component
public class ChatRunner implements ApplicationRunner {

  private final ChatService chatService;

  public ChatRunner(ChatService chatService) {
    this.chatService = chatService;
  }

  @Override
  public void run(ApplicationArguments args) {
    chatService.enterCharRoom("test-chat");
  }

}
