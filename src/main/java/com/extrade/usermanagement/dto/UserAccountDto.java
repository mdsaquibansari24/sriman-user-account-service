package com.extrade.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data

public class UserAccountDto {
    @JsonIgnore
    private int userAccountId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String emailAddress;
    private String password;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dob;
    private String gender;
    @JsonIgnore
    private int mobileNoOtpVerificationStatus;
    @JsonIgnore
    private int emailAddressOtpVerificationStatus;
    @JsonIgnore
    private String status;

}
