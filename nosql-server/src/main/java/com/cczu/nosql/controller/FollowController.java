package com.cczu.nosql.controller;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowerResponse;
import com.cczu.nosql.response.FollowingResponse;
import com.cczu.nosql.result.PageResult;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.FollowService;
import com.cczu.nosql.util.SessionContext;
import com.cczu.nosql.validation.annotation.MeUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * 关注控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/follow")
public class FollowController {


	final FollowService followService;

	public FollowController(FollowService followService) {
		this.followService = followService;
	}

	/**
	 * 用户是否关注目标用户
	 * @param fromUserId 用户ID，可传 "me" 表示当前登录人
	 * @param toUserId   目标用户ID
	 * @return 关注状态
	 */
	@Parameter(in = ParameterIn.PATH, name = "fromUserId", description = "用户ID，可传 \"me\" 表示当前登录人", required = true)
	@Parameter(in = ParameterIn.PATH, name = "toUserId", description = "目标用户ID", required = true)
	@Operation(summary = "通用：某用户是否关注目标用户", description = "通用：某用户是否关注目标用户")
	@GetMapping("/{fromUserId}/following/{toUserId}")
	public Result<FollowStateResponse> isFollowing(@MeUserId @PathVariable String fromUserId, @PathVariable Long toUserId) {
		log.info("查询用户 {} 是否关注 用户 {}", fromUserId, toUserId);
		Long fromUid = "me".equals(fromUserId) ? SessionContext.getSession().getUserId() : Long.valueOf(fromUserId);
		return Result.success(followService.exists(fromUid, toUserId));
	}

	/**
	 * 用户关注/取消关注目标用户
	 * @param fromUserId 用户ID，可传 "me" 表示当前登录人
	 * @param toUserId   目标用户ID
	 * @return 关注状态
	 */
	@Parameter(in = ParameterIn.PATH, name = "fromUserId", description = "用户ID，可传 \"me\" 表示当前登录人", required = true)
	@Parameter(in = ParameterIn.PATH, name = "toUserId", description = "目标用户ID", required = true)
	@Operation(summary = "通用：某用户关注/取消关注目标用户", description = "通用：某用户关注/取消关注目标用户")
	@PostMapping("/{fromUserId}/following/{toUserId}")
	public Result<FollowStateResponse> followUser(@MeUserId @PathVariable String fromUserId, @PathVariable Long toUserId) {
		log.info("用户 {} 关注/取关 用户 {}", fromUserId, toUserId);
		Long fromUid = "me".equals(fromUserId) ? SessionContext.getSession().getUserId() : Long.valueOf(fromUserId);
		if (Objects.equals(fromUid, toUserId)) {
			return Result.fail(BizCode.OPERATION_FAILED, "不能关注自己");
		}
		//todo 考虑扩展管理员
		if(!Objects.equals(fromUid, SessionContext.getSession().getUserId())) {
			return Result.fail(BizCode.OPERATION_FAILED, "只能操作自己的关注关系");
		}
		return Result.success(followService.toggleFollow(SessionContext.getSession().getUserId(), toUserId));
	}

	/**
	 * 查询用户关注列表
	 * @param userId 用户ID，可传 "me" 表示当前登录人
	 * @return 关注列表
	 */
	@Parameter(in = ParameterIn.PATH, name = "userId", description = "用户ID，可传 \"me\" 表示当前登录人", required = true)
	@Operation(summary = "查询用户关注列表", description = "查询用户关注列表")
	@GetMapping("/{userId}/followings")
	public Result<PageResult<FollowingResponse>> getUserFollowings(@MeUserId @PathVariable String userId, @Valid PageParam pageParam) {
		log.info("查询用户 {} 关注列表", userId);
		Long uid = "me".equals(userId) ? SessionContext.getSession().getUserId() : Long.valueOf(userId);
		return Result.success(followService.getUserFollowings(uid, pageParam));
	}

	/**
	 * 查询用户粉丝列表
	 * @param userId 用户ID，可传 "me" 表示当前登录人
	 * @return 粉丝列表
	 */
	@Parameter(in = ParameterIn.PATH, name = "userId", description = "用户ID，可传 \"me\" 表示当前登录人", required = true)
	@Operation(summary = "查询用户粉丝列表", description = "查询用户粉丝列表")
	@GetMapping("/{userId}/followers")
	public Result<PageResult<FollowerResponse>> getUserFollowers(@MeUserId @PathVariable String userId, @Valid PageParam pageParam) {
		log.info("查询用户 {} 粉丝列表", userId);
		Long uid = "me".equals(userId) ? SessionContext.getSession().getUserId() : Long.valueOf(userId);
		return Result.success(followService.getUserFollowers(uid, pageParam));
	}
}
