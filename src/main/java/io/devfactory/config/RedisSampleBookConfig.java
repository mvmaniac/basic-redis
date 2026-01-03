package io.devfactory.config;

import io.devfactory.sample.book.cache.CacheKeyGenerator;
import io.devfactory.sample.book.lock.LockKey;
import io.devfactory.sample.book.lock.LockKeySerializer;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.data.redis.autoconfigure.DataRedisProperties;
import org.springframework.boot.jackson.JacksonComponentModule;
import org.springframework.boot.jackson.JacksonMixinModule;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import tools.jackson.databind.DefaultTyping;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.jsontype.BasicPolymorphicTypeValidator;

import java.time.Duration;
import java.util.HashMap;

@EnableCaching
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class RedisSampleBookConfig {

  private final DataRedisProperties redisProperties;

  @Bean
  public RedisConnectionFactory bookRedisConnectionFactory() {
    // RedisSentinelConfiguration, RedisClusterConfiguration 등 상황에 맞게 사용 필요
    final var redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    redisStandaloneConfiguration.setUsername(redisProperties.getUsername());
    redisStandaloneConfiguration.setPassword(redisProperties.getPassword());

    // 필요한 경우 사용
    // redisStandaloneConfiguration.setUsername("");
    // redisStandaloneConfiguration.setPassword("");

    // 레디스와 클라이언트 사이에 커넥션을 생성할 때 소요되는 최대 시간, 기본값 10초
    final SocketOptions socketOptions = SocketOptions.builder().connectTimeout(Duration.ofSeconds(SocketOptions.DEFAULT_CONNECT_TIMEOUT)).build();
    final ClientOptions clientOptions = ClientOptions.builder().socketOptions(socketOptions).build();

    final var clientConfiguration = LettuceClientConfiguration.builder()
        .clientOptions(clientOptions)
        .commandTimeout(Duration.ofSeconds(5)) // 레디스 명령어를 실행하고 응답받는 시간, 기본값 60초
        .shutdownTimeout(Duration.ZERO) // 레디스 클라이언트가 안전하게 종료하려고 애플리케이션이 종료될 때까지 기다리는 최대 시간, 기본값 0.1초(100밀리)
        .build();

    return new LettuceConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
  }

  // cache value serialize 설정 방법 (redisTemplate 사용시에도 동일함)
  //
  // 1. 기본 (byte[])
  // Serializable 인터페이스 구현 필요, 그리고 테스트 시에는 spring-boot-devtools 의존성 제거 필요 (ClassCastException 발생)
  // Object의 Class Type이 함께 저장되는 듯(?), 그러면 같은 패키지 경로에 해당 클래스 파일이 있어야 함 (다른 시스템에서도 맞추면 상관없으나, 혹시 패키지 경로를 변경한다면 문제 발생)
  //
  // 2. GenericJackson2JsonRedisSerializer
  // JSON 문자열로 저장되나 1번과 마찬가지로 Object의 Class Type을 함께 저장함, 동일한 문제를 같음
  //
  // 3. Jackson2JsonRedisSerializer
  // Object의 Class Type을 함께 저장하지는 않지만 Class Type를 매번 지정해서 함 (쓰레드간 접근 문제 발생, cache 별로 cacheManager를 생성하면 되기는 할듯...)
  //
  // 4. StringRedisSerializer
  // 그냥 JSON 문자열로 넣고 가져올 떄 직접 parse하는 방법, 패키지 문제나 쓰레드간 접근 문제 발생하지 않음
  @Bean
  public RedisCacheManager bookCacheManager(RedisConnectionFactory bookRedisConnectionFactory) {
    final var defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
        .disableCachingNullValues()
        .entryTtl(Duration.ofHours(1)) // 캐시데이터 유효기간, 기본 TTL (Time To Live)
        .computePrefixWith(CacheKeyPrefix.simple())
        .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

    // 특정캐시에 대한 설정, RedisCacheConfiguration.defaultCacheConfig는 불변객체이므로 entryTtl값을 변경해도 새로운 객체로 반환함
    final var stringCacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofMinutes(10))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));

    final var jsonCacheConfiguration = defaultCacheConfig.entryTtl(Duration.ofMinutes(5))
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJacksonJsonRedisSerializer(redisObjectMapper())));

    final var configMap = new HashMap<String, RedisCacheConfiguration>();
    configMap.put(RedisCacheKey.BOOK_CACHE, stringCacheConfiguration);
    configMap.put(RedisCacheKey.BOOK_CACHE_JSON, jsonCacheConfiguration);

    return RedisCacheManager
        .RedisCacheManagerBuilder
        .fromConnectionFactory(bookRedisConnectionFactory)
        .cacheDefaults(defaultCacheConfig)
        .withInitialCacheConfigurations(configMap)
        .build();
  }

  public ObjectMapper redisObjectMapper() {
    final var ptv = BasicPolymorphicTypeValidator
        .builder()
        .allowIfSubType(Object.class)
        .build();

    return JsonMapper.builder()
        .addModule(new JacksonMixinModule())
        .addModule(new JacksonComponentModule())
        .activateDefaultTyping(ptv, DefaultTyping.NON_FINAL)
        .build();
  }

  @Bean
  public CacheKeyGenerator cacheKeyGenerator() {
    return new CacheKeyGenerator();
  }

  @Bean
  public RedisTemplate<LockKey, Long> lockRedisTemplate(
      RedisConnectionFactory bookRedisConnectionFactory) {
    RedisTemplate<LockKey, Long> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(bookRedisConnectionFactory);
    redisTemplate.setKeySerializer(new LockKeySerializer());
    redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
    return redisTemplate;
  }

  @Bean
  public RedisTemplate<String, Long> rankRedisTemplate(
      RedisConnectionFactory bookRedisConnectionFactory) {
    RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(bookRedisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
    return redisTemplate;
  }

}
