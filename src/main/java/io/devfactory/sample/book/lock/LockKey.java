package io.devfactory.sample.book.lock;

public class LockKey {

  private static final String PREFIX = "LOCK::";

  private final Long id;

  private LockKey(Long id) {
    if (null == id) {
      throw new IllegalArgumentException("id can not be null...");
    }
    this.id = id;
  }

  public static LockKey of(Long id) {
    return new LockKey(id);
  }

  public static LockKey of(String key) {
    String idToken = key.substring(PREFIX.length());
    Long id = Long.valueOf(idToken);
    return LockKey.of(id);
  }

  @Override
  public String toString() {
    return PREFIX + id;
  }

}
