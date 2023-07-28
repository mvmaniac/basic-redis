package io.devfactory.sample.book.lock;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/book/lock")
@RestController
public class LockController {

  private final LockService lockService;

  @GetMapping("{id}/{userId}")
  public ResponseEntity<Boolean> attachEvent(@PathVariable Long id, @PathVariable Long userId) {
    return ResponseEntity.ok(lockService.attachEvent(id, userId));
  }

}
