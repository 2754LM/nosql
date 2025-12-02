package com.cczu.nosql.response;

import com.cczu.nosql.entity.User;
import lombok.Data;

@Data
public class FollowingResponse {
	private long userId;
	private List<User> followings;
}
