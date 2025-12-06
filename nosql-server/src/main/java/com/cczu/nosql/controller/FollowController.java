package com.cczu.nosql.controller;

import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.response.FollowStateResponse;
import com.cczu.nosql.response.FollowerResponse;
import com.cczu.nosql.response.FollowingResponse;
import com.cczu.nosql.result.PageResult;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.FollowService;
import com.cczu.nosql.util.SessionContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/** 关注控制器 */
@Tag(name = "关注控制器", description = "关注控制器")
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
   *
   * @param toUserId 目标用户ID
   * @return 关注状态
   */
  @Parameters({
    @Parameter(name = "fromUserId", description = "用户ID", in = ParameterIn.PATH, required = true),
    @Parameter(name = "toUserId", description = "目标用户ID", in = ParameterIn.PATH, required = true)
  })
  @Operation(summary = "通用：某用户是否关注目标用户", description = "通用：某用户是否关注目标用户")
  @GetMapping("/{toUserId}")
  public Result<FollowStateResponse> isFollowing(@PathVariable Long toUserId) {
    Long fromUserId = SessionContext.getSession().getUserId();
    log.info("查询用户 {} 是否关注 用户 {}", fromUserId, toUserId);
    return Result.success(followService.exists(fromUserId, toUserId));
  }

  /**
   * 用户关注/取消关注目标用户
   *
   * @param toUserId 目标用户ID
   * @return 关注状态
   */
  @Parameters({
    @Parameter(name = "fromUserId", description = "用户ID", in = ParameterIn.PATH, required = true),
    @Parameter(name = "toUserId", description = "目标用户ID", in = ParameterIn.PATH, required = true)
  })
  @Operation(summary = "通用：某用户关注/取消关注目标用户", description = "通用：某用户关注/取消关注目标用户")
  @PostMapping("/{toUserId}")
  public Result<FollowStateResponse> followUser(@PathVariable Long toUserId) {
    Long fromUserId = SessionContext.getSession().getUserId();
    log.info("用户 {} 关注/取关 用户 {}", fromUserId, toUserId);
    return Result.success(followService.toggleFollow(fromUserId, toUserId));
  }

  /**
   * 查询用户关注列表
   *
   * @param userId 用户ID
   * @return 关注列表
   */
  @Parameter(name = "userId", description = "用户ID", in = ParameterIn.PATH, required = true)
  @Operation(summary = "查询用户关注列表", description = "查询用户关注列表")
  @GetMapping("/{userId}/followings")
  public PageResult<FollowingResponse> getUserFollowings(
      @PathVariable Long userId, @Valid PageParam pageParam) {
    log.info("查询用户 {} 关注列表", userId);
    return PageResult.success(followService.getUserFollowings(userId, pageParam), pageParam);
  }

  /**
   * 查询用户粉丝列表
   *
   * @param userId 用户ID
   * @return 粉丝列表
   */
  @Parameter(name = "userId", description = "用户ID", in = ParameterIn.PATH, required = true)
  @Operation(summary = "查询用户粉丝列表", description = "查询用户粉丝列表")
  @GetMapping("/{userId}/followers")
  public PageResult<FollowerResponse> getUserFollowers(
      @PathVariable Long userId, @Valid PageParam pageParam) {
    log.info("查询用户 {} 粉丝列表", userId);
    return PageResult.success(followService.getUserFollowers(userId, pageParam), pageParam);
  }
}
