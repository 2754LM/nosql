package com.cczu.nosql.service.impl;

import com.cczu.nosql.entity.User;
import com.cczu.nosql.entity.UserFollow;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowerResponse;
import com.cczu.nosql.response.FollowingResponse;
import com.cczu.nosql.response.UserInfoResponse;
import com.cczu.nosql.result.PageResult;
import com.cczu.nosql.service.FollowService;
import io.ebean.DB;
import io.ebean.PagedList;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FollowServiceImpl implements FollowService {
	@Override
	public FollowStateResponse toggleFollow(Long uid, Long toUserId) {
		boolean exists = DB.find(UserFollow.class).where().eq("fromUser.id", uid).eq("toUser.id", toUserId).exists();
		if (exists) {
			DB.find(UserFollow.class).where().eq("fromUser.id", uid).eq("toUser.id", toUserId).delete();
			return new FollowStateResponse(false);
		} else {
			UserFollow userFollow = new UserFollow(uid, toUserId);
			DB.save(userFollow);
			return new FollowStateResponse(true);
		}
	}

	@Override
	public FollowStateResponse exists(Long uid, Long toUserId) {
		return DB.find(UserFollow.class).where().eq("fromUser.id", uid).eq("toUser.id", toUserId).exists()
				? new FollowStateResponse(true)
				: new FollowStateResponse(false);
	}

	@Override
	public PageResult<FollowerResponse> getUserFollowers(Long uid, PageParam pageParam) {
		PagedList<UserFollow> pagedList = DB.find(UserFollow.class)
				.fetch("fromUser")
				.where()
				.eq("toUser.id", uid)
				.orderBy("id desc")
				.setFirstRow((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()))
				.setMaxRows((int) pageParam.getSize())
				.findPagedList();

		List<Long> followerIds = pagedList.getList().stream()
				.map(uf -> uf.getFromUser().getId())
				.toList();

		Set<Long> mutualIdSet = DB.find(UserFollow.class)
				.where()
				.eq("fromUser.id", uid)
				.in("toUser.id", followerIds)
				.findList()
				.stream()
				.map(uf -> uf.getToUser().getId())
				.collect(Collectors.toSet());

		List<FollowerResponse> followerResponses = pagedList.getList().stream()
				.map(uf -> {
					User follower = uf.getFromUser();
					UserInfoResponse userInfo =
							new UserInfoResponse(follower.getId(), follower.getName());
					boolean isMutual = mutualIdSet.contains(follower.getId());
					return new FollowerResponse(userInfo, isMutual);
				})
				.toList();

		return PageResult.of(followerResponses, pagedList);
	}


	@Override
	public PageResult<FollowingResponse> getUserFollowings(Long uid, PageParam pageParam) {
		PagedList<UserFollow> pagedList = DB.find(UserFollow.class)
				.fetch("toUser")
				.where()
				.eq("fromUser.id", uid)
				.orderBy("id desc")
				.setFirstRow((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()))
				.setMaxRows((int) pageParam.getSize())
				.findPagedList();

		List<Long> followingIds = pagedList.getList().stream()
				.map(uf -> uf.getToUser().getId())
				.toList();

		Set<Long> mutualIdSet = DB.find(UserFollow.class)
				.where()
				.in("fromUser.id", followingIds)
				.eq("toUser.id", uid)
				.findList()
				.stream()
				.map(uf -> uf.getFromUser().getId())
				.collect(Collectors.toSet());

		List<FollowingResponse> followingResponses = pagedList.getList().stream()
				.map(uf -> {
					User following = uf.getToUser();
					UserInfoResponse userInfo =
							new UserInfoResponse(following.getId(), following.getName());
					boolean isMutual = mutualIdSet.contains(following.getId());
					return new FollowingResponse(userInfo, isMutual);
				})
				.toList();

		return PageResult.of(followingResponses, pagedList);
	}

}
