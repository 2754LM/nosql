package com.cczu.nosql.service.impl;

import com.cczu.nosql.service.CacheService;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CacheServiceImpl implements CacheService {
	final RedissonClient redissonClient;

	public CacheServiceImpl(RedissonClient redissonClient) {
		this.redissonClient = redissonClient;
	}

	@Override
	public Object getValue(String key) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		return bucket.get();
	}

	@Override
	public <T> T getValue(String key, Class<T> clazz) {
		Object val = getValue(key);
		if (val == null) return null;
		return clazz.cast(val);
	}

	@Override
	public void setValue(String key, Object value) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(value);
	}

	@Override
	public void setValue(String key, Object value, Duration ttl) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(value, ttl);
	}

	@Override
	public void deleteValue(String key) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.delete();
	}

	@Override
	public boolean exists(String key) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		return bucket.isExists();
	}

	@Override
	public long increment(String key, long delta) {
		RAtomicLong atomic = redissonClient.getAtomicLong(key);
		return atomic.addAndGet(delta);
	}

	@Override
	public long decrement(String key, long delta) {
		RAtomicLong atomic = redissonClient.getAtomicLong(key);
		return atomic.addAndGet(-Math.abs(delta));
	}

	@Override
	public void expire(String key, Duration ttl) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.expire(ttl);
	}

	@Override
	public Set<String> keys(String pattern) {
		RKeys rKeys = redissonClient.getKeys();
		Iterable<String> it = rKeys.getKeysByPattern(pattern);
		Set<String> result = new HashSet<>();
		for (String k : it) {
			result.add(k);
		}
		return result;
	}

	@Override
	public void clear() {
		redissonClient.getKeys().deleteByPattern("*");
	}
}
