package io.devfactory.sample.book.cache;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

// 역직렬화 되려면 기본 생성자 필요
@NoArgsConstructor
@Getter
@Setter
public class CacheResponse {

  private Long id;
  private String name;
  private String address;
  private LocalDateTime date;

  public CacheResponse(Long id, String name, String address, LocalDateTime date) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.date = date;
  }

}
