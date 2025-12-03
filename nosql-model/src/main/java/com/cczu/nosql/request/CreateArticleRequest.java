package com.cczu.nosql.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateArticleRequest {
	@NotBlank(message = "标题不能为空")
	@Size(max = 32, message = "标题长度不能超过32个字符")
	private String title;
	@NotBlank(message = "内容不能为空")
	private String content;
}
