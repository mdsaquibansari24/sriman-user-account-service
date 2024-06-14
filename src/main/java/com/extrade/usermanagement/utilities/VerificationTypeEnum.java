package com.extrade.usermanagement.utilities;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VerificationTypeEnum {
    VERIFY_MOBILE("verifyMobile"),
    VERIFY_EMAIL_ADDRESS("verifyEmail");

    private String name;

    @Override
    public String toString() {
        return this.name;
    }
}
