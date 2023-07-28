package io.devfactory.sample.book.cache;

public class CacheKey {

  private CacheKey() {
    throw new IllegalStateException("Constant only class, no constructor support...");
  }

  public static final String BOOK_CACHE = "book-cache";
  public static final String BOOK_CACHE_JSON = "book-cache-json";

}
