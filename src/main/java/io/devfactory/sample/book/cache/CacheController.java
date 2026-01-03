package io.devfactory.sample.book.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@RequestMapping("/book/cache")
@RestController
public class CacheController {

  private final CacheService cacheService;
  private final ObjectMapper objectMapper;

  @GetMapping("/{id}")
  public ResponseEntity<CacheResponse> getCacheById(
      @PathVariable Long id) {
    String jsonStr = cacheService.getCacheById(id);
    return ResponseEntity.ok(getCacheResponseResponse(jsonStr));
  }


  @GetMapping("/{id}/json")
  public ResponseEntity<CacheResponse> getCacheJsonById(@PathVariable Long id) {
    return ResponseEntity.ok(cacheService.getCacheJsonById(id));
  }

  @PostMapping("/string")
  public ResponseEntity<CacheResponse> postCache(
      @RequestBody CacheRequest cacheRequest) {
    String jsonStr = cacheService.postCache(cacheRequest);
    return ResponseEntity.ok(getCacheResponseResponse(jsonStr));
  }

  @PostMapping("/json")
  public ResponseEntity<CacheResponse> postCacheJson(@RequestBody CacheRequest cacheRequest) {
    return ResponseEntity.ok(cacheService.postCacheJson(cacheRequest));
  }

  @DeleteMapping("/{id}/string")
  public ResponseEntity<Object> deleteCacheById(@PathVariable Long id) {
    cacheService.deleteCacheById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<CacheResponse> getAndPostCache(@RequestBody CacheRequest request) {
    return ResponseEntity.ok(cacheService.getAndPostCache(request));
  }

  private CacheResponse getCacheResponseResponse(String jsonStr) {
    if (null == jsonStr) {
      return new CacheResponse();
    }
    return objectMapper.readValue(jsonStr, CacheResponse.class);
  }

}
