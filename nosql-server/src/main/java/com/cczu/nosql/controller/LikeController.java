package com.cczu.nosql.controller;

import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/** 点赞控制器 */
@Tag(name = "点赞控制器", description = "点赞控制器")
@RestController
@RequestMapping("/api/like")
public class LikeController {
  final LikeService likeService;

  public LikeController(LikeService likeService) {
    this.likeService = likeService;
  }

  /**
   * 点赞/取消点赞文章
   *
   * @param articleId
   * @return
   */
  @Parameter(name = "articleId", description = "", in = ParameterIn.PATH, required = true)
  @Operation(summary = "点赞/取消点赞文章", description = "点赞/取消点赞文章")
  @PostMapping("/{articleId}")
  public Result<Void> likeArticle(@PathVariable Long articleId) {
    likeService.likeArticle(articleId);
    return Result.success();
  }

  /**
   * 用户是否点赞文章
   *
   * @param articleId
   * @return
   */
  @Parameter(name = "articleId", description = "", in = ParameterIn.PATH, required = true)
  @Operation(summary = "用户是否点赞文章", description = "用户是否点赞文章")
  @GetMapping("/{articleId}")
  public Result<Boolean> isArticleLiked(@PathVariable Long articleId) {
    return Result.success(likeService.isArticleLiked(articleId));
  }
}
