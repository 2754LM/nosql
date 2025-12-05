package com.cczu.nosql.Aspect;

import com.cczu.nosql.annotation.CheckUserPermission;
import com.cczu.nosql.constant.BizCode;
import com.cczu.nosql.exception.BizException;
import com.cczu.nosql.util.SessionContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CheckUserPermissionAspect {

  @Around("@annotation(permissionAnnotation)")
  public Object validateUserPermission(
      ProceedingJoinPoint joinPoint, CheckUserPermission permissionAnnotation) throws Throwable {
    try {
      log.info("执行用户权限校验切面，拦截方法: {}", joinPoint.getSignature().toShortString());
      // test
      if (SessionContext.getSession() == null) {
        SessionContext.setUid(1L);
      }
      Long currentUserId = SessionContext.getSession().getUserId();
      if (permissionAnnotation == null
          || permissionAnnotation.value() == null
          || permissionAnnotation.value().trim().isEmpty()) {
        throw new BizException(BizCode.PARAM_NOT_NULL);
      }
      Long targetId = getTargetId(joinPoint, permissionAnnotation.value());
      if (!Objects.equals(currentUserId, targetId)) {
        throw new BizException(BizCode.NO_PERMISSION);
      }
      return joinPoint.proceed();
    } catch (BizException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new BizException(BizCode.PATH_NOT_FOUND);
    }
  }

  private Long getTargetId(ProceedingJoinPoint joinPoint, String path) throws Exception {
    String[] segments = path.split("\\.");
    if (segments.length > 2) {
      throw new BizException(BizCode.PARAM_TYPE_ERROR);
    }
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    String[] paramNames = methodSignature.getParameterNames();
    Object[] paramValues = joinPoint.getArgs();
    Object targetParam = null;
    for (int i = 0; i < paramNames.length; i++) {
      if (Objects.equals(paramNames[i], segments[0])) {
        targetParam = paramValues[i];
        break;
      }
    }
    Object targetValue = targetParam;
    if (segments.length == 2) {
      targetValue = getFieldValue(targetParam, segments[1]);
    }
    return (Long) targetValue;
  }

  private Object getFieldValue(Object obj, String fieldName)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String getterName = "get" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
    Method getter = obj.getClass().getMethod(getterName);
    return getter.invoke(obj);
  }
}
