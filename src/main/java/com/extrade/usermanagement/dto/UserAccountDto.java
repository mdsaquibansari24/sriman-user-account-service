package com.extrade.usermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder(builderMethodName = "of")
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
    private int userAccountId;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String emailAddress;
    private String password;
    @JsonFormat(pattern = "yyyy/MM/dd")
    private LocalDate dob;
    private String gender;
    private String roleCode;
    private String status;

}
