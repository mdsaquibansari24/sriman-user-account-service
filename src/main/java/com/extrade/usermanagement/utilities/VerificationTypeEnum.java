package com.extrade.usermanagement.utilities;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VerificationTypeEnum {
    VERIFICATION_TYPE_MOBILE("VERIFICATION_TYPE_MOBILE"),
    VERIFICATION_TYPE_EMAIL_ADDRESS("VERIFICATION_TYPE_EMAIL_ADDRESS");

    private String name;
}
