package com.extrade.usermanagement.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserAccountDto {
    private int userAccountId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String emailAddress;
    private String password;
    private LocalDate dob;
    private String gender;
    private int mobileNoOtpVerificationStatus;
    private int emailAddressOtpVerificationStatus;
    private String status;

}
