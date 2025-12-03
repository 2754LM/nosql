package com.cczu.nosql.controller;

import com.cczu.nosql.entity.Article;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.response.FullArticleResponse;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.ArticleService;
import com.cczu.nosql.validation.annotation.MeUserId;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 文章控制器
 */
@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

	@Autowired
	ArticleService articleService;

	@PostMapping("/save")
	public Result<Void> createArticle(@Valid @RequestBody CreateArticleRequest request) {
		log.info("创建文章，标题：{}，内容：{}", request.getTitle(), request.getContent().substring(0, Math.min(request.getContent().length(), 20)) + "...");
		articleService.save(request);
		return Result.success();
	}

	@GetMapping("/{userId}/list")
	public Result<FullArticleResponse> getUserArticles(@MeUserId @PathVariable Long userId) {
		log.info("查询用户文章列表，用户ID：{}", userId);
//		List<FullArticleResponse> articles = articleService.getUserArticles(userId);
		return Result.success();
	}

}
