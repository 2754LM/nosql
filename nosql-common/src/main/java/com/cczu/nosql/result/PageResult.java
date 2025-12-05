package com.cczu.nosql.result;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.request.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(hidden = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageResult<T> extends BaseResult {
  // 记录列表
  @Schema(description = "记录列表")
  private List<T> records;

  // 每页记录数
  @Schema(description = "每页记录数")
  private long size;

  // 当前页，从1开始
  @Schema(description = "当前页，从1开始")
  private long current;

  public static <T> PageResult<T> success(List<T> records, PageParam pageParam) {
    PageResult<T> result = new PageResult<>();
    result.setCode(BizCode.SUCCESS.getCode());
    result.setMessage(BizCode.SUCCESS.getMessage());
    result.setRecords(records);
    if (pageParam != null) {
      result.setSize(pageParam.getSize());
      result.setCurrent(pageParam.getCurrent());
    }
    return result;
  }
}
