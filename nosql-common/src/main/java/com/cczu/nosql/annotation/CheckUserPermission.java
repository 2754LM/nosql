package com.cczu.nosql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 校验方法中指定的用户权限 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckUserPermission {

  /** 需要校验的参数名数组 默认校验名为"userId"的参数 */
  String value() default "userId";
}
