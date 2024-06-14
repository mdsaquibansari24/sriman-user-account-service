package com.extrade.usermanagement.utilities;

public interface ErrorCodes {
    String USER_NOT_FOUND = "user.notFound";
    String USER_ALREADY_ACTIVATED = "user.alreadyActivated";
    String VERIFICATION_CODE_MISMATCH = "optCode.mismatch";
    String OTPCODE_ALREADY_VERIFIED = "otpCode.alreadyVerified";
}
