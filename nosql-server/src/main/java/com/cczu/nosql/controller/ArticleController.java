package com.cczu.nosql.controller;

import com.cczu.nosql.annotation.CheckUserPermission;
import com.cczu.nosql.request.ArticleQueryParam;
import com.cczu.nosql.request.CreateArticleRequest;
import com.cczu.nosql.request.PageParam;
import com.cczu.nosql.request.UpdateArticleRequest;
import com.cczu.nosql.response.FullArticleResponse;
import com.cczu.nosql.result.PageResult;
import com.cczu.nosql.result.Result;
import com.cczu.nosql.service.ArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 文章控制器
 */
@Tag(name = "文章控制器", description = "文章控制器")
@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

	final ArticleService articleService;

	public ArticleController(ArticleService articleService) {
		this.articleService = articleService;
	}

	/**
	 * 创建文章
	 * @param request 创建文章请求
	 * @return 操作结果
	 */
	@Operation(summary = "创建文章", description = "创建文章")
	@PostMapping("/create")
	public Result<Void> createArticle(@Valid @RequestBody CreateArticleRequest request) {
		log.info("创建文章，标题：{}，内容：{}", request.getTitle(), request.getContent().substring(0, Math.min(request.getContent().length(), 20)) + "...");
		articleService.create(request);
		return Result.success();
	}

	/**
	 * 搜索文章
	 * @param queryParam 查询参数
	 * @param pageParam 分页参数
	 * @return 文章列表
	 */
	@Operation(summary = "搜索文章", description = "搜索文章")
	@GetMapping("/search")
	public PageResult<FullArticleResponse> searchArticles(@Valid ArticleQueryParam queryParam,@Valid PageParam pageParam) {
		log.info("搜索文章，参数：{}", queryParam);
		return PageResult.success(articleService.searchArticles(queryParam, pageParam), pageParam);
	}

	/**
	 * 更新文章
	 * @param request 更新文章请求
	 * @return 操作结果
	 */
	@Operation(summary = "更新文章", description = "更新文章")
	@CheckUserPermission(value = "request.authorId")
	@PostMapping("/update")
	public Result<Void> updateArticle(@Valid @RequestBody UpdateArticleRequest request){
		log.info("更新文章，ID：{}，标题：{}", request.getId(), request.getTitle());
		articleService.updateArticle(request);
		return Result.success();
	}
}
