package com.cczu.nosql.controller;

import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/** 点赞控制器 */
@RestController
@RequestMapping("/api/like")
public class LikeController {
  @Autowired LikeService likeService;

  @PostMapping("/{articleId}")
  public Result<Void> likeArticle(@PathVariable Long articleId) {
    likeService.likeArticle(articleId);
    return Result.success();
  }

  @GetMapping("/{articleId}")
  public Result<Boolean> isArticleLiked(@PathVariable Long articleId) {
    return Result.success(likeService.isArticleLiked(articleId));
  }
}
