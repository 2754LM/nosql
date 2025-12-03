package com.cczu.nosql.validation.validator;

import com.cczu.nosql.validation.annotation.MeUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MeUserIdValidator implements ConstraintValidator<MeUserId, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null) return false;
		return value.equals("me") || value.matches("\\d+");
	}
}
