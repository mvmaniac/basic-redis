package io.devfactory.basic._02_session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

// 서버 포트를 다르게 한 후 서버 2개를 띄워서 테스트
@RequestMapping("/login-user")
@RestController
public class LoginUserController {

  private final ConcurrentHashMap<String, String> sessionMap = new ConcurrentHashMap<>();

  // 세션으로 로그인 처리 (가정)
  @GetMapping("/session/login")
  public String loginInSession(@RequestParam String name, HttpSession session) {
    sessionMap.put(session.getId(), name);
    return "session saved";
  }

  // 세션에서 로그인한 사용자 정보 가져오기 (가정)
  @GetMapping("/session/user")
  public String userInSession(HttpSession session) {
    return sessionMap.getOrDefault(session.getId(), "not login");
  }

  // 레디스를 사용하여 세션으로 로그인 처리 (가정)
  @GetMapping("/redis/login")
  public String loginInRedis(@RequestParam String name, HttpSession session) {
    session.setAttribute("user", name);
    return "redis saved";
  }

  // 레디스를 사용하여 세션에서 로그인한 사용자 정보 가져오기 (가정)
  @GetMapping("/redis/user")
  public String userInRedis(HttpSession session) {
    return Objects.toString(session.getAttribute("user"), "not login");
  }

}
