package com.extrade.usermanagement.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderMethodName = "of")
public class AccountVerificationStatusDto {
    private int userAccountId;
    private int mobileVerificationStatus;
    private int emailVerificationStatus;
    private String mobileNo;
    private String emailAddress;
    private String accountStatus;
}
