package com.cczu.nosql.service.impl;

import com.cczu.nosql.service.RedisService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {

  private final RedissonClient redissonClient;
  private final ObjectMapper objectMapper;

  public RedisServiceImpl(RedissonClient redissonClient, ObjectMapper objectMapper) {
    this.redissonClient = redissonClient;
    this.objectMapper = objectMapper;
  }

  /* -------- String / Value -------- */

  @Override
  public Object get(String key) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    return bucket.get();
  }

  @Override
  public <T> T get(String key, Class<T> type) {
    Object value = get(key);
    if (value == null) return null;
    // 如果是 Map（来自 JSON 编解码），使用 ObjectMapper 转换为目标类型
    if (value instanceof Map) {
      return objectMapper.convertValue(value, type);
    }
    // 如果是字符串且目标不是 String，尝试反序列化
    if (value instanceof String && type != String.class) {
      try {
        return objectMapper.readValue((String) value, type);
      } catch (Exception ignore) {
        // 回退到强转
      }
    }
    return type.cast(value);
  }

  @Override
  public <T> void set(String key, T value) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    bucket.set(value);
  }

  @Override
  public <T> void set(String key, T value, Duration ttl) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    bucket.set(value, ttl);
  }

  @Override
  public boolean del(String key) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    return bucket.delete();
  }

  @Override
  public boolean exists(String key) {
    RBucket<Object> bucket = redissonClient.getBucket(key);
    return bucket.isExists();
  }

  @Override
  public long incrBy(String key, long delta) {
    RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
    return atomicLong.addAndGet(delta);
  }

  @Override
  public long decrBy(String key, long delta) {
    RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
    return atomicLong.addAndGet(-Math.abs(delta));
  }

  /* -------- Key 批量 -------- */

  @Override
  public Set<String> scan(String pattern) {
    RKeys keys = redissonClient.getKeys();
    Iterable<String> iterable = keys.getKeysByPattern(pattern);
    Set<String> result = new HashSet<>();
    for (String key : iterable) {
      result.add(key);
    }
    return result;
  }

  @Override
  public long clearAll() {
    return redissonClient.getKeys().deleteByPattern("*");
  }

  @Override
  public Map<String, Object> mGet(List<String> keys) {
    if (keys == null || keys.isEmpty()) {
      return Collections.emptyMap();
    }
    RBatch batch = redissonClient.createBatch();
    for (String key : keys) {
      batch.getBucket(key).getAsync();
    }
    BatchResult<?> batchResult = batch.execute();
    List<?> responses = batchResult.getResponses();
    Map<String, Object> result = new HashMap<>(keys.size());
    for (int i = 0; i < keys.size(); i++) {
      Object value = responses.get(i);
      if (value != null) {
        result.put(keys.get(i), value);
      }
    }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Map<String, T> mGet(List<String> keys, Class<T> type) {
    Map<String, Object> raw = mGet(keys);
    Map<String, T> result = new HashMap<>(raw.size());
    for (Map.Entry<String, Object> entry : raw.entrySet()) {
      Object value = entry.getValue();
      if (value == null) continue;

      try {
        T converted = null;

        // 数字类型安全转换
        if (value instanceof Number num) {
          if (type == Long.class) {
            converted = (T) Long.valueOf(num.longValue());
          } else if (type == Integer.class) {
            converted = (T) Integer.valueOf(num.intValue());
          } else if (type == Double.class) {
            converted = (T) Double.valueOf(num.doubleValue());
          } else if (type == Float.class) {
            converted = (T) Float.valueOf(num.floatValue());
          } else if (type == Short.class) {
            converted = (T) Short.valueOf(num.shortValue());
          } else if (type == Byte.class) {
            converted = (T) Byte.valueOf(num.byteValue());
          } else {
            if (type.isInstance(value)) {
              converted = (T) value;
            }
          }
        } else if (type.isInstance(value)) {
          converted = (T) value;
        } else if (value instanceof Map) {
          // 关键：如果是 Map（JSON 反序列化后），使用 ObjectMapper 转换为目标类型
          converted = objectMapper.convertValue(value, type);
        } else {
          // 回退：尝试通过字符串解析常见类型或使用 ObjectMapper 反序列化
          String s = value.toString();
          if (type == String.class) {
            converted = (T) s;
          } else {
            try {
              converted = objectMapper.readValue(s, type);
            } catch (Exception e) {
              // 尝试基础类型解析
              if (type == Long.class) {
                converted = (T) Long.valueOf(s);
              } else if (type == Integer.class) {
                converted = (T) Integer.valueOf(s);
              } else if (type == Double.class) {
                converted = (T) Double.valueOf(s);
              } else if (type == Float.class) {
                converted = (T) Float.valueOf(s);
              } else if (type == Short.class) {
                converted = (T) Short.valueOf(s);
              } else if (type == Byte.class) {
                converted = (T) Byte.valueOf(s);
              } else {
                // 无法转换，跳过该 key
                continue;
              }
            }
          }
        }

        if (converted != null) {
          result.put(entry.getKey(), converted);
        }
      } catch (Exception ignore) {
        continue;
      }
    }
    return result;
  }


  @Override
  public <T> BatchResult<?> mSet(Map<String, T> keyValues) {
    if (keyValues == null || keyValues.isEmpty()) {
      return null;
    }
    RBatch batch = redissonClient.createBatch();
    for (Map.Entry<String, T> entry : keyValues.entrySet()) {
      batch.getBucket(entry.getKey()).setAsync(entry.getValue());
    }
    return batch.execute();
  }

  /* -------- Sorted Set (ZSet) -------- */

  @Override
  public <T> boolean zAdd(String key, double score, T member) {
    RScoredSortedSet<T> zset = redissonClient.getScoredSortedSet(key);
    return zset.add(score, member);
  }

  @Override
  public <T> long zAddAll(String key, Map<T, Double> scoreMembers) {
    RScoredSortedSet<T> zset = redissonClient.getScoredSortedSet(key);
    return zset.addAll(new HashMap<>(scoreMembers));
  }

  @Override
  public <T> List<T> zRangeByIndex(String key, int start, int end, boolean desc, Class<T> type) {
    RScoredSortedSet<T> zset = redissonClient.getScoredSortedSet(key);
    Collection<T> values = desc ? zset.valueRangeReversed(start, end) : zset.valueRange(start, end);
    return new ArrayList<>(values);
  }

  @Override
  public long zCard(String key) {
    RScoredSortedSet<Object> zset = redissonClient.getScoredSortedSet(key);
    return zset.size();
  }

  @Override
  public <T> double zIncrBy(String key, double delta, T member) {
    RScoredSortedSet<T> zset = redissonClient.getScoredSortedSet(key);
    return zset.addScore(member, delta);
  }

  @Override
  public void zRem(String key, String member) {
    RScoredSortedSet<String> zset = redissonClient.getScoredSortedSet(key);
    zset.remove(member);
  }

  /* -------- Hash -------- */

  @Override
  public <T> Object hSet(String key, String field, T value) {
    RMap<String, Object> map = redissonClient.getMap(key);
    return map.put(field, value);
  }

  @Override
  public <T> T hGet(String key, String field, Class<T> type) {
    RMap<String, Object> map = redissonClient.getMap(key);
    Object value = map.get(field);
    if (value == null) return null;
    if (value instanceof Map) {
      return objectMapper.convertValue(value, type);
    }
    if (value instanceof String && type != String.class) {
      try {
        return objectMapper.readValue((String) value, type);
      } catch (Exception ignore) {
      }
    }
    return type.cast(value);
  }

  @Override
  public Object hDel(String key, String field) {
    RMap<String, Object> map = redissonClient.getMap(key);
    return map.remove(field);
  }

  @Override
  public boolean hExists(String key, String field) {
    RMap<String, Object> map = redissonClient.getMap(key);
    return map.containsKey(field);
  }

  @Override
  public Map<String, Object> hGetAll(String key) {
    RMap<String, Object> map = redissonClient.getMap(key);
    return map.readAllMap();
  }

  @Override
  public <T> Map<String, T> hGetAll(String key, Class<T> type) {
    RMap<String, Object> map = redissonClient.getMap(key);
    Map<String, Object> raw = map.readAllMap();
    Map<String, T> result = new HashMap<>(raw.size());
    for (Map.Entry<String, Object> entry : raw.entrySet()) {
      Object v = entry.getValue();
      if (v instanceof Map) {
        result.put(entry.getKey(), objectMapper.convertValue(v, type));
      } else {
        result.put(entry.getKey(), type.cast(v));
      }
    }
    return result;
  }

  @Override
  public long hLen(String key) {
    RMap<String, Object> map = redissonClient.getMap(key);
    return map.size();
  }

  /* -------- Set -------- */

  @Override
  public <T> boolean sAdd(String key, T member) {
    RSet<T> set = redissonClient.getSet(key);
    return set.add(member);
  }

  @Override
  public <T> boolean sRem(String key, T member) {
    RSet<T> set = redissonClient.getSet(key);
    return set.remove(member);
  }

  @Override
  public <T> boolean sIsMember(String key, T member) {
    RSet<T> set = redissonClient.getSet(key);
    return set.contains(member);
  }

  @Override
  public <T> Set<T> sMembers(String key, Class<T> type) {
    RSet<T> set = redissonClient.getSet(key);
    return new HashSet<>(set);
  }

  /* -------- Queue (普通队列) -------- */

  @Override
  public <T> boolean queuePush(String key, T value) {
    RQueue<T> queue = redissonClient.getQueue(key);
    return queue.add(value);
  }

  @Override
  public <T> T queuePoll(String key, Class<T> type) {
    RQueue<T> queue = redissonClient.getQueue(key);
    T value = queue.poll();
    if (value == null) return null;
    return type.cast(value);
  }

  @Override
  public int queueSize(String key) {
    RQueue<Object> queue = redissonClient.getQueue(key);
    return queue.size();
  }

  @Override
  public boolean queueClear(String key) {
    RQueue<Object> queue = redissonClient.getQueue(key);
    return queue.delete();
  }

  /* -------- BlockingQueue -------- */

  @Override
  public <T> boolean bQueuePush(String key, T value) {
    RBlockingQueue<T> queue = redissonClient.getBlockingQueue(key);
    return queue.add(value);
  }

  @Override
  public <T> T bQueueTake(String key, Class<T> type) throws InterruptedException {
    RBlockingQueue<T> queue = redissonClient.getBlockingQueue(key);
    T value = queue.take();
    return type.cast(value);
  }

  @Override
  public <T> T bQueuePoll(String key, long timeout, TimeUnit unit, Class<T> type)
      throws InterruptedException {
    RBlockingQueue<T> queue = redissonClient.getBlockingQueue(key);
    T value = queue.poll(timeout, unit);
    if (value == null) return null;
    return type.cast(value);
  }
}
