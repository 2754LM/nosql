package com.cczu.nosql.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CacheService {
  Object getValue(String key);

  <T> T getValue(String key, Class<T> clazz);

  <T> void setValue(String key, T value);

  void deleteValue(String key);

  boolean exists(String key);

  long increment(String key, long delta);

  long decrement(String key, long delta);

  Set<String> keys(String pattern);

  void clear();

  Map<String, Object> multiGet(List<String> keys);

  <T> Map<String, T> multiGet(List<String> keys, Class<T> clazz);

  <T> void multiSet(Map<String, T> keyValues);

  <T> boolean zAdd(String key, double score, T value);

  <T> long zAdd(String key, Map<T, Double> scoreMembers);

  <T> List<T> zRange(String key, int start, int end, boolean reverse, Class<T> clazz);

  long zCard(String key);

  <T> double zincrby(String key, double increment, T member);
}
