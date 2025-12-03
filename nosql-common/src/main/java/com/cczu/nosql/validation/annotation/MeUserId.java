package com.cczu.nosql.validation.annotation;

import com.cczu.nosql.validation.validator.MeUserIdValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 用户ID路径变量校验：只能为 "me" 或纯数字
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MeUserIdValidator.class)
public @interface MeUserId {

	String message() default "userId 只能为 me 或数字";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}
