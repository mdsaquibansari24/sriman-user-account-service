package com.extrade.usermanagement.utilities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserAccountStatusEnum {
    ACTIVE("A"),
    LOCKED("L"),
    DISABLED("D");

    @Getter
    private final String name;

    @Override
    public String toString() {
        return name;
    }
}
