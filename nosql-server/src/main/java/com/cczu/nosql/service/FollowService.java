package com.cczu.nosql.service;

import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowersResponse;
import com.cczu.nosql.response.FollowingsResponse;

public interface FollowService {
	FollowStateResponse toggleFollow(Long uid, Long toUserId);

	FollowStateResponse exists(Long uid, Long toUserId);

	FollowersResponse getUserFollowers(Long uid);

	FollowingsResponse getUserFollowings(Long uid);
}
