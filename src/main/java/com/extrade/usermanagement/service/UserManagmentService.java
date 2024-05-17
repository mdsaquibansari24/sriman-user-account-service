package com.extrade.usermanagement.service;

import com.extrade.usermanagement.dto.AccountVerificationStatusDto;
import com.extrade.usermanagement.dto.UserAccountDto;
import com.extrade.usermanagement.utilities.VerificationTypeEnum;

public interface UserManagmentService {
    long countUsersByEmailAddress(String emailAddress);

    long countUsersByMobileNo(String mobileNo);

    long registerCustomer(UserAccountDto userAccountDto);

    AccountVerificationStatusDto verifyOtpAndUpdateAccountStatus(int userAccountId, String verificationCode, VerificationTypeEnum verificationType);
}
