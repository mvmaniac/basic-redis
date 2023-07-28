package io.devfactory.sample.book.lock;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class LockKeySerializer implements RedisSerializer<LockKey> {

  @Override
  public byte[] serialize(LockKey lockKey) throws SerializationException {
    if (null == lockKey) {
      throw new SerializationException("lockKey is null");
    }

    return lockKey.toString().getBytes();
  }

  @Override
  public LockKey deserialize(byte[] bytes) throws SerializationException {
    if (null == bytes) {
      throw new SerializationException("bytes is null");
    }

    return LockKey.of(new String(bytes));
  }

}
