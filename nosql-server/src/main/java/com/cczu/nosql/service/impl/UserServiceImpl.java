package com.cczu.nosql.service.impl;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.service.UserService;
import io.ebean.DB;
import io.ebean.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User getById(Long id) {
		return DB.find(User.class).where().eq("id", id).findOne();
	}

	@Override
	public User getByName(String name) {
		return DB.find(User.class).where().eq("name", name).findOne();
	}

	@Override
	public void save(User user) {
		DB.save(user);
	}
}
