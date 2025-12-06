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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/** 文章控制器 */
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
   *
   * @param request 创建文章请求
   * @return 操作结果
   */
  @Operation(summary = "创建文章", description = "创建文章")
  @PostMapping
  public Result<Void> createArticle(@Valid @RequestBody CreateArticleRequest request) {
    log.info(
        "创建文章，标题：{}，内容：{}",
        request.getTitle(),
        request.getContent().substring(0, Math.min(request.getContent().length(), 20)) + "...");
    articleService.create(request);
    return Result.success();
  }

  /**
   * 搜索文章
   *
   * @param queryParam 查询参数
   * @param pageParam 分页参数
   * @return 文章列表
   */
  @Operation(summary = "搜索文章", description = "搜索文章")
  @GetMapping("/search")
  public PageResult<FullArticleResponse> searchArticles(
      @ModelAttribute @Valid ArticleQueryParam queryParam,
      @ModelAttribute @Valid PageParam pageParam) {
    log.info("搜索文章，参数：{}, {}", queryParam, pageParam);
    return PageResult.success(articleService.searchArticles(queryParam, pageParam), pageParam);
  }

  /**
   * 更新文章
   *
   * @param request 更新文章请求
   * @return 操作结果
   */
  @Parameter(name = "id", description = "", in = ParameterIn.PATH, required = true)
  @Operation(summary = "更新文章", description = "更新文章")
  @CheckUserPermission(value = "request.authorId")
  @PutMapping("/{id}")
  public Result<Void> updateArticle(
      @PathVariable Long id, @Valid @RequestBody UpdateArticleRequest request) {
    log.info("更新文章，ID：{}，标题：{}", request.getId(), request.getTitle());
    articleService.updateArticle(id, request);
    return Result.success();
  }

  /**
   * 删除文章
   *
   * @param id 文章ID
   * @return 操作结果
   */
  @Parameter(name = "id", description = "文章ID", in = ParameterIn.PATH, required = true)
  @Operation(summary = "删除文章", description = "删除文章")
  @DeleteMapping("/{id}")
  public Result<Void> deleteArticle(@PathVariable Long id) {
    log.info("删除文章，ID：{}", id);
    articleService.deleteArticle(id);
    return Result.success();
  }

  /**
   * 获取热门文章
   *
   * @param limit 数量限制
   * @return 热门文章列表
   */
  @Parameter(name = "limit", description = "数量限制", in = ParameterIn.QUERY, required = true)
  @Operation(summary = "获取热门文章", description = "获取热门文章")
  @GetMapping("/hot")
  public Result<List<FullArticleResponse>> searchHotArticles(
      @RequestParam(required = false, defaultValue = "10") @Valid @Min(1) @Max(100) int limit) {
    log.info("获取热门文章，数量限制：{}", limit);
    return Result.success(articleService.searchArticles(limit));
  }
}
