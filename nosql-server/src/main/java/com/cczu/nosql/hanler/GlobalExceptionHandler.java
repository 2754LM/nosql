package com.cczu.nosql.hanler;

import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.result.Result;
import jakarta.persistence.PersistenceException;
import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // 业务异常
  @ExceptionHandler(BizException.class)
  public Result<Void> handleBiz(BizException ex) {
    log.error("业务异常：{}", ex.getMessage());
    return Result.fail(ex.getCode(), ex.getMessage());
  }

  // 参数校验
  @ExceptionHandler({
    MethodArgumentNotValidException.class,
    HttpMessageNotReadableException.class,
    ConstraintViolationException.class,
    HandlerMethodValidationException.class,
    TypeMismatchException.class
  })
  public Result<Void> handleParam(Exception ex) {
    log.warn("参数非法: {}", ex.getMessage());
    return Result.fail(BizCode.PARAM_INVALID);
  }

  // 请求路径不存在
  @ExceptionHandler({NoResourceFoundException.class})
  public Result<Void> handleNoHandlerFoundException(NoResourceFoundException ex) {
    log.warn("请求路径不存在: {}", ex.getMessage());
    return Result.fail(BizCode.PATH_NOT_FOUND);
  }

  // 捕获数据库异常
  @ExceptionHandler({SQLException.class, PersistenceException.class})
  public Result<Void> handleEbeanException(PersistenceException ex) {
    log.error("数据库异常：", ex);
    return Result.fail(BizCode.DB_ERROR);
  }

  // 捕获其他异常
  @ExceptionHandler(Exception.class)
  public Result<Void> handleException(Exception ex) {
    log.error("未知异常：", ex);
    return Result.fail(BizCode.UNKNOWN_ERROR);
  }
}
