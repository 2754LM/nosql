package com.cczu.nosql.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema
@Data
public class CreateArticleRequest {
  // 标题
  @Schema(description = "标题")
  @NotBlank(message = "标题不能为空")
  @Size(max = 32, message = "标题长度不能超过32个字符")
  private String title;

  // 内容
  @Schema(description = "内容")
  @NotBlank(message = "内容不能为空")
  private String content;
}
