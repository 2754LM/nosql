package com.cczu.nosql.service.impl;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.entity.UserFollow;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowersResponse;
import com.cczu.nosql.response.FollowingsResponse;
import com.cczu.nosql.response.UserInfoResponse;
import com.cczu.nosql.service.FollowService;
import io.ebean.DB;
import io.ebean.Database;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public FollowersResponse getUserFollowers(Long uid) {
		List<UserFollow> userFollows = DB.find(UserFollow.class)
				.fetch("fromUser")
				.where()
				.eq("toUser.id", uid)
				.findList();
		List<UserInfoResponse> followers = userFollows.stream()
				.map(uf -> {
					User u = uf.getFromUser();
					return new UserInfoResponse(u.getId(), u.getName());
				})
				.collect(Collectors.toList());
		return new FollowersResponse(uid, followers);
	}

	@Override
	public FollowingsResponse getUserFollowings(Long uid) {
		List<UserFollow> userFollows = DB.find(UserFollow.class)
				.fetch("toUser")
				.where()
				.eq("fromUser.id", uid)
				.findList();
		List<UserInfoResponse> followings = userFollows.stream()
				.map(uf -> {
					User u = uf.getToUser();
					return new UserInfoResponse(u.getId(), u.getName());
				})
				.collect(Collectors.toList());
		return new FollowingsResponse(uid, followings);
	}
}
