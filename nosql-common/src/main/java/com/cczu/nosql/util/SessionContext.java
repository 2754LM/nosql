package com.cczu.nosql.util;

import com.cczu.nosql.session.Session;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class SessionContext {
	private static final ThreadLocal<Session> userThreadLocal = new ThreadLocal<>();
	public static void setUid(Long userId) {
		userThreadLocal.set(new Session(userId, 1));
	}

	public static void setSession(Session session) {
		userThreadLocal.set(session);
	}

	public static Session getSession() {
		return userThreadLocal.get();
	}

	public static void clear() {
		userThreadLocal.remove();
	}


}
