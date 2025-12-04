package com.cczu.nosql.service;

import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowerResponse;
import com.cczu.nosql.response.FollowingResponse;
import com.cczu.nosql.result.PageResult;

import java.util.List;

public interface FollowService {
	FollowStateResponse toggleFollow(Long uid, Long toUserId);

	FollowStateResponse exists(Long uid, Long toUserId);

	List<FollowerResponse> getUserFollowers(Long uid, PageParam pageParam);

	List<FollowingResponse> getUserFollowings(Long uid, PageParam pageParam);
}
