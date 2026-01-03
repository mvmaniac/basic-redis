package io.devfactory.config;

public class RedisCacheKey {

  private RedisCacheKey() {
    throw new IllegalStateException("Constant only class, no constructor support...");
  }

  public static final String USER_AGE = "user-age";
  public static final String BOOK_CACHE = "book-cache";
  public static final String BOOK_CACHE_JSON = "book-cache-json";

}
