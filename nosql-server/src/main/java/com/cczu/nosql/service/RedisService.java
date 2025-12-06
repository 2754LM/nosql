package com.cczu.nosql.service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.redisson.api.BatchResult;

public interface RedisService {

  /* -------- String / Value -------- */

  /**
   * 获取指定 key 的值。
   *
   * @param key Redis key
   * @return 对应的值，不存在时返回 \`null\`
   */
  Object get(String key);

  /**
   * 获取指定 key 的值，并按类型进行转换。
   *
   * @param key Redis key
   * @param type 目标类型
   * @param <T> 泛型类型
   * @return 转换后的值，不存在时返回 \`null\`
   */
  <T> T get(String key, Class<T> type);

  /**
   * 设置指定 key 的值（无过期时间）。
   *
   * @param key Redis key
   * @param value 要设置的值
   * @param <T> 值类型
   */
  <T> void set(String key, T value);

  /**
   * 设置指定 key 的值（带过期时间）。
   *
   * @param key Redis key
   * @param value 要设置的值
   * @param timeout 过期时间
   * @param <T> 值类型
   */
  <T> void set(String key, T value, Duration timeout);

  /**
   * 删除指定 key。
   *
   * @param key Redis key
   * @return 是否删除成功
   */
  boolean del(String key);

  /**
   * 判断 key 是否存在。
   *
   * @param key Redis key
   * @return \`true\` 存在，\`false\` 不存在
   */
  boolean exists(String key);

  /**
   * 自增（或自减）数值 key。
   *
   * @param key Redis key
   * @param delta 增量，正数为自增，负数为自减
   * @return 操作后的最新值
   */
  long incrBy(String key, long delta);

  /**
   * 自减数值 key（语义上为 decr，但底层仍是 addAndGet 负数）。
   *
   * @param key Redis key
   * @param delta 减少的量
   * @return 操作后的最新值
   */
  long decrBy(String key, long delta);

  /* -------- Key 批量 -------- */

  /**
   * 按模式扫描 key。
   *
   * @param pattern 通配符模式，比如 \`prefix:*\`
   * @return 匹配到的 key 集合
   */
  Set<String> scan(String pattern);

  /**
   * 清空当前 Redis 实例下的所有 key（通过通配符删除）。
   *
   * @return 删除的 key 数量
   */
  long clearAll();

  /**
   * 批量获取多个 key 的值。
   *
   * @param keys key 列表
   * @return key → value 映射（不存在的 key 不会出现在 map 中）
   */
  Map<String, Object> mGet(List<String> keys);

  /**
   * 批量获取多个 key 的值，并按类型转换。
   *
   * @param keys key 列表
   * @param type 目标类型
   * @param <T> 泛型类型
   * @return key → 转换后值 的映射
   */
  <T> Map<String, T> mGet(List<String> keys, Class<T> type);

  /**
   * 批量设置多个 key 的值。
   *
   * @param keyValues key → value 映射
   * @param <T> 值类型
   * @return Redisson 批处理结果，可能为 \`null\`（当入参为空时）
   */
  <T> BatchResult<?> mSet(Map<String, T> keyValues);

  /* -------- Sorted Set (ZSet) -------- */

  /**
   * 向有序集合中添加一个成员。
   *
   * @param key zset key
   * @param score 分数
   * @param member 成员值
   * @param <T> 成员类型
   * @return 是否成功新增（已存在则返回 false）
   */
  <T> boolean zAdd(String key, double score, T member);

  /**
   * 批量向有序集合中添加成员。
   *
   * @param key zset key
   * @param scoreMembers 成员 → 分数 映射
   * @param <T> 成员类型
   * @return 新增的成员数量
   */
  <T> long zAddAll(String key, Map<T, Double> scoreMembers);

  /**
   * 按下标区间获取有序集合成员。
   *
   * @param key zset key
   * @param start 起始下标（从 0 开始）
   * @param end 结束下标（包含）
   * @param desc 是否倒序，\`true\` 为 score 从大到小
   * @param type 成员类型
   * @param <T> 成员类型
   * @return 成员列表
   */
  <T> List<T> zRangeByIndex(String key, int start, int end, boolean desc, Class<T> type);

  /**
   * 获取有序集合成员数量。
   *
   * @param key zset key
   * @return 元素个数
   */
  long zCard(String key);

  /**
   * 为指定成员的分数增加（或减少）。
   *
   * @param key zset key
   * @param delta 分数增量
   * @param member 成员
   * @param <T> 成员类型
   * @return 增加后的最新分数
   */
  <T> double zIncrBy(String key, double delta, T member);

  /**
   * 从有序集合中移除指定成员。
   *
   * @param key zset key
   * @param member 成员值
   */
  void zRem(String key, String member);

  /* -------- Hash -------- */

  /**
   * 向 hash 中写入字段值。
   *
   * @param key hash key
   * @param field 字段名
   * @param value 字段值
   * @param <T> 值类型
   * @return 旧值（若存在），否则为 \`null\`
   */
  <T> Object hSet(String key, String field, T value);

  /**
   * 从 hash 中读取字段值。
   *
   * @param key hash key
   * @param field 字段名
   * @param type 值类型
   * @param <T> 值类型
   * @return 字段值，不存在则为 \`null\`
   */
  <T> T hGet(String key, String field, Class<T> type);

  /**
   * 删除 hash 中的字段。
   *
   * @param key hash key
   * @param field 字段名
   * @return 被删除的旧值
   */
  Object hDel(String key, String field);

  /**
   * 判断 hash 中字段是否存在。
   *
   * @param key hash key
   * @param field 字段名
   * @return \`true\` 存在，\`false\` 不存在
   */
  boolean hExists(String key, String field);

  /**
   * 读取 hash 中的所有字段和值。
   *
   * @param key hash key
   * @return field → value 映射
   */
  Map<String, Object> hGetAll(String key);

  /**
   * 读取 hash 中的所有字段，并按类型转换。
   *
   * @param key hash key
   * @param type 值类型
   * @param <T> 值类型
   * @return field → 转换后值 的映射
   */
  <T> Map<String, T> hGetAll(String key, Class<T> type);

  /**
   * 获取 hash 中字段数量。
   *
   * @param key hash key
   * @return 字段个数
   */
  long hLen(String key);

  /* -------- Set -------- */

  /**
   * 向集合添加一个成员。
   *
   * @param key set key
   * @param member 成员
   * @param <T> 成员类型
   * @return 是否为新添加（已存在返回 false）
   */
  <T> boolean sAdd(String key, T member);

  /**
   * 从集合中移除一个成员。
   *
   * @param key set key
   * @param member 成员
   * @param <T> 成员类型
   * @return 是否移除成功
   */
  <T> boolean sRem(String key, T member);

  /**
   * 判断成员是否存在于集合中。
   *
   * @param key set key
   * @param member 成员
   * @param <T> 成员类型
   * @return \`true\` 存在，\`false\` 不存在
   */
  <T> boolean sIsMember(String key, T member);

  /**
   * 获取集合中所有成员。
   *
   * @param key set key
   * @param type 成员类型
   * @param <T> 成员类型
   * @return 成员集合（已做拷贝）
   */
  <T> Set<T> sMembers(String key, Class<T> type);

  /* -------- Queue (普通队列) -------- */

  /**
   * 向普通队列尾部插入元素。
   *
   * @param key 队列 key
   * @param value 元素
   * @param <T> 元素类型
   * @return 是否插入成功
   */
  <T> boolean queuePush(String key, T value);

  /**
   * 从普通队列头部弹出一个元素。
   *
   * @param key 队列 key
   * @param type 元素类型
   * @param <T> 元素类型
   * @return 元素，不存在时为 \`null\`
   */
  <T> T queuePoll(String key, Class<T> type);

  /**
   * 获取队列当前长度。
   *
   * @param key 队列 key
   * @return 队列长度
   */
  int queueSize(String key);

  /**
   * 清空并删除队列。
   *
   * @param key 队列 key
   * @return 是否删除成功
   */
  boolean queueClear(String key);

  /* -------- BlockingQueue -------- */

  /**
   * 向阻塞队列插入元素。
   *
   * @param key 队列 key
   * @param value 元素
   * @param <T> 元素类型
   * @return 是否插入成功
   */
  <T> boolean bQueuePush(String key, T value);

  /**
   * 从阻塞队列中阻塞式获取一个元素（没有则一直阻塞）。
   *
   * @param key 队列 key
   * @param type 元素类型
   * @param <T> 元素类型
   * @return 获取到的元素
   * @throws InterruptedException 线程被中断
   */
  <T> T bQueueTake(String key, Class<T> type) throws InterruptedException;

  /**
   * 从阻塞队列中在超时时间内获取一个元素。
   *
   * @param key 队列 key
   * @param timeout 超时时间
   * @param unit 时间单位
   * @param type 元素类型
   * @param <T> 元素类型
   * @return 元素，超时或为空时返回 \`null\`
   * @throws InterruptedException 线程被中断
   */
  <T> T bQueuePoll(String key, long timeout, TimeUnit unit, Class<T> type)
      throws InterruptedException;
}
