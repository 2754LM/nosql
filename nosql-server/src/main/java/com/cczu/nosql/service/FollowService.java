package com.cczu.nosql.service;

import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowerResponse;
import com.cczu.nosql.response.FollowingResponse;
import com.cczu.nosql.result.PageResult;

public interface FollowService {
	FollowStateResponse toggleFollow(Long uid, Long toUserId);

	FollowStateResponse exists(Long uid, Long toUserId);

	PageResult<FollowerResponse> getUserFollowers(Long uid, PageParam pageParam);

	PageResult<FollowingResponse> getUserFollowings(Long uid, PageParam pageParam);
}
