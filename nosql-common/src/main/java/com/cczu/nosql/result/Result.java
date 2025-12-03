package com.cczu.nosql.result;

import com.cczu.nosql.constant.BizCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(hidden = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
	//状态码
	@Schema(description = "状态码")
	private int code;
	//提示信息
	@Schema(description = "提示信息")
	private String message;
	//返回数据
	@Schema(description = "返回数据")
	private T data;
	//错误详情
	@Schema(description = "错误详情")
	private String detail = "";

	public static <T> Result<T> success() {
		return new Result<>(BizCode.SUCCESS.getCode(), BizCode.SUCCESS.getMessage(), null, "");
	}

	public static <T> Result<T> success(T data) {
		return new Result<>(BizCode.SUCCESS.getCode(), BizCode.SUCCESS.getMessage(), data, "");
	}

	public static <T> Result<T> success(T data, String message) {
		return new Result<>(BizCode.SUCCESS.getCode(), message, data, "");
	}


	public static <T> Result<T> success(String message) {
		return new Result<>(BizCode.SUCCESS.getCode(), message, null, "");
	}

	public static <T> Result<T> fail(int code, String message) {
		return new Result<>(code, message, null, "");
	}

	public static <T> Result<T> fail(BizCode bizCode) {
		return new Result<>(bizCode.getCode(), bizCode.getMessage(), null, "");
	}

	public static <T> Result<T> fail(BizCode bizCode, String detail) {
		return new Result<>(bizCode.getCode(), bizCode.getMessage(), null, detail);
	}
}
