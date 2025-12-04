package com.cczu.nosql.request;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema
@Data
public class ArticleQueryParam {
	// 文章标题
	@Schema(description = "文章标题")
	private String title;
	// 文章内容
	@Schema(description = "文章内容")
	private String content;
	// 作者ID
	@Schema(description = "作者ID")
	private Long authorId;
	// 文章ID
	@Schema(description = "文章ID")
	private Long id;
	// 开始日期
	@Schema(description = "开始日期")
	private LocalDateTime startDate;
	// 结束日期
	@Schema(description = "结束日期")
	private LocalDateTime endDate;
	// 排序字段
	@Schema(description = "排序字段")
	private ArticleSortField sort = ArticleSortField.UPDATE_TIME;
	// 是否降序
	@Schema(description = "是否降序")
	private Boolean desc = true;
}