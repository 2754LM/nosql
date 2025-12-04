package com.cczu.nosql.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResult {
	@Schema(description = "状态码")
	private int code;

	@Schema(description = "提示信息")
	private String message;

}