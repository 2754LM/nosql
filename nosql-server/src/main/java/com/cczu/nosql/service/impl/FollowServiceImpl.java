package com.cczu.nosql.service.impl;

import com.cczu.nosql.entity.UserFollow;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.service.FollowService;
import io.ebean.DB;
import io.ebean.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {
	@Override
	public FollowStateResponse toggleFollow(Long uid, Long toUserId) {
		boolean exists = DB.find(UserFollow.class).where().eq("fromUserId", uid).eq("toUserId", toUserId).exists();
		if (exists) {
			DB.find(UserFollow.class).where().eq("fromUserId", uid).eq("toUserId", toUserId).delete();
			return new FollowStateResponse(false);
		} else {
			UserFollow userFollow = new UserFollow(uid, toUserId);
			DB.save(userFollow);
			return new FollowStateResponse(true);
		}
	}

	@Override
	public FollowStateResponse exists(Long uid, Long toUserId) {
		return DB.find(UserFollow.class).where().eq("fromUserId", uid).eq("toUserId", toUserId).exists()
				? new FollowStateResponse(true)
				: new FollowStateResponse(false);
	}
}
