package com.extrade.usermanagement.exceptions;

import com.extrade.usermanagement.utilities.VerificationTypeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class AccountVerificationException extends RuntimeException {
    @Getter
    private final VerificationTypeEnum verificationType;

    public AccountVerificationException(String message, VerificationTypeEnum verificationType) {
        super(message);
        this.verificationType = verificationType;
    }
}
