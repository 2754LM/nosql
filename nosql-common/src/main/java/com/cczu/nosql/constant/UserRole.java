package com.cczu.nosql.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    GUEST(0),
    USER(1),
    ADMIN(2);

    private final int value;
}