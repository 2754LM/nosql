package com.cczu.nosql.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Schema
@Data
public class PageParam {
  // 页码
  @Schema(description = "页码")
  @Min(1)
  private long current = 1;

  // 页大小
  @Schema(description = "页大小")
  @Min(1)
  @Max(1000)
  private long size = 20;
}
