package com.cczu.nosql.service;

import java.time.Duration;
import java.util.Set;

public interface CacheService {
	Object getValue(String key);

	<T> T getValue(String key, Class<T> clazz);

	void setValue(String key, Object value);

	void setValue(String key, Object value, Duration ttl);

	void deleteValue(String key);

	boolean exists(String key);

	long increment(String key, long delta);

	long decrement(String key, long delta);

	void expire(String key, Duration ttl);

	Set<String> keys(String pattern);

	void clear();
}
