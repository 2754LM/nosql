package com.cczu.nosql.service;

import com.cczu.nosql.response.FollowStateResponse;

public interface FollowService {
	FollowStateResponse toggleFollow(Long uid, Long toUserId);

	FollowStateResponse exists(Long uid, Long toUserId);


}
