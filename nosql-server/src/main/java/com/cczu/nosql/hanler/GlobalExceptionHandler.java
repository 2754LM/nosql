package com.cczu.nosql.hanler;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
		log.warn("参数校验失败：{}", ex.getMessage());
		return Result.fail(BizCode.PARAM_INVALID);
	}

	//捕获请求体异常
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		log.warn("请求体异常：{}", ex.getMessage());
		return Result.fail(BizCode.PARAM_INVALID);
	}
	//捕获其他异常
	@ExceptionHandler(Exception.class)
	public Result<Void> handleException(Exception ex) {
		log.error("未知异常：", ex);
		return Result.fail(BizCode.UNKNOWN_ERROR, "系统繁忙，请稍后重试");
	}
}