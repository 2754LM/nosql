package com.cczu.nosql.result;

import com.cczu.nosql.constant.BizCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
public class Result<T> extends BaseResult {
	@Schema(description = "返回数据")
	private T data;

	public Result(int code, String message, T data) {
		super(code, message);
		this.data = data;
	}

	public static <T> Result<T> success(T data) {
		return new Result<>(BizCode.SUCCESS.getCode(),
				BizCode.SUCCESS.getMessage(),
				data);
	}

	public static <T> Result<T> fail(BizCode bizCode) {
		return new Result<>(bizCode.getCode(),
				bizCode.getMessage(),
				null);
	}

	public static Result<Void> fail(int code, String message) {
		return new Result<>(code, message, null);
	}

	public static Result<Void> success() {
		return new Result<>(BizCode.SUCCESS.getCode(),
				BizCode.SUCCESS.getMessage(),
				null);
	}
}