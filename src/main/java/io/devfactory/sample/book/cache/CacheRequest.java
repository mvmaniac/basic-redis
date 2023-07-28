package io.devfactory.sample.book.cache;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class CacheRequest {

  private final Long id;
  private final String name;
  private final String address;
  private final LocalDateTime date;

  public CacheRequest(Long id, String name, String address, LocalDateTime date) {
    this.id = id;
    this.name = name;
    this.address = address;
    this.date = date;
  }

}
