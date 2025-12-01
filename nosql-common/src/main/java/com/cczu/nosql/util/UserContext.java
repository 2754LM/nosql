package com.cczu.nosql.util;

public class UserContext {
	private static final ThreadLocal<String> userThreadLocal = new ThreadLocal<>();
	public static void setUid(String userId) {
		userThreadLocal.set(userId);
	}
	public static String getUid() {
		return userThreadLocal.get();
	}
	public static void clear() {
		userThreadLocal.remove();
	}
}
