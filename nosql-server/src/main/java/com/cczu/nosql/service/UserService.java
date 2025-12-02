package com.cczu.nosql.service;

import com.cczu.nosql.entity.User;

public interface UserService {
	User getById(Long id);

	User getByName(String name);

	void save(User user);
}
