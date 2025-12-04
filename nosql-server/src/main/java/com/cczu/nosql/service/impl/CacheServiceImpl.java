package com.cczu.nosql.service.impl;

import com.cczu.nosql.service.CacheService;
import org.redisson.api.*;
import org.springframework.stereotype.Service;

import java.util.*;

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
	public <T> void setValue(String key, T value) {
		RBucket<Object> bucket = redissonClient.getBucket(key);
		bucket.set(value);
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

	@Override
	public Map<String, Object> multiGet(List<String> keys) {
		RBatch batch = redissonClient.createBatch();
		Map<String, Object> result = new HashMap<>(keys.size());
		for (String key : keys) {
			batch.getBucket(key).getAsync();
		}
		List<?> responses = batch.execute().getResponses();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			Object value = responses.get(i);
			if (value != null) {
				result.put(key, value);
			}
		}
		return result;
	}

	@Override
	public <T> Map<String, T> multiGet(List<String> keys, Class<T> clazz) {
		Map<String, Object> rawResult = multiGet(keys);
		Map<String, T> result = new HashMap<>(rawResult.size());
		for (Map.Entry<String, Object> entry : rawResult.entrySet()) {
			if (entry.getValue() != null) {
				result.put(entry.getKey(), clazz.cast(entry.getValue()));
			}
		}
		return result;
	}

	@Override
	public <T> void multiSet(Map<String, T> keyValues) {
		RBatch batch = redissonClient.createBatch();
		for (Map.Entry<String, T> entry : keyValues.entrySet()) {
			batch.getBucket(entry.getKey()).setAsync(entry.getValue());
		}
		batch.execute();
	}

	@Override
	public <T> boolean zAdd(String key, double score, T value) {
		RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
		return sortedSet.add(score, value);
	}

	@Override
	public <T> long zAdd(String key, Map<T, Double> scoreMembers) {
		RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
		Map<T, Double> scoreMap = new HashMap<>(scoreMembers);
		return sortedSet.addAll(scoreMap);
	}

	@Override
	public <T> List<T> zRange(String key, int start, int end, boolean reverse, Class<T> clazz) {
		RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
		Collection<T> collection;
		if (reverse) {
			collection = sortedSet.valueRangeReversed(start, end);
		} else {
			collection = sortedSet.valueRange(start, end);
		}
		return new ArrayList<>(collection);
	}


	@Override
	public long zCard(String key) {
		RScoredSortedSet<Object> sortedSet = redissonClient.getScoredSortedSet(key);
		return sortedSet.size();
	}

	@Override
	public <T> double zincrby(String key, double increment, T member) {
		RScoredSortedSet<T> sortedSet = redissonClient.getScoredSortedSet(key);
		return sortedSet.addScore(member, increment);
	}

}
