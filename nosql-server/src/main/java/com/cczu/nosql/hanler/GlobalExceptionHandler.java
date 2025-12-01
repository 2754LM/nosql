package com.cczu.nosql.hanler;

import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	//捕获自定义异常
	@ExceptionHandler(BizException.class)
	public Result<Void> handleBiz(BizException ex) {
		log.error("业务异常：{}", ex.getMessage());
		return Result.fail(ex.getCode(), ex.getMessage());
	}

	//捕获参数校验异常
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result<Void> handleValid(MethodArgumentNotValidException ex) {
		// 取第一条字段错误信息返回
		String msg = ex.getBindingResult()
				.getFieldErrors()
				.getFirst()
				.getDefaultMessage();
		log.warn("参数校验失败：{}", msg);
		return Result.fail(1001, msg);
	}

	//捕获其他异常
	@ExceptionHandler(Exception.class)
	public Result<Void> handleException(Exception ex) {
		log.error("未知异常：", ex);
		return Result.fail(-1, "系统异常");
	}
}