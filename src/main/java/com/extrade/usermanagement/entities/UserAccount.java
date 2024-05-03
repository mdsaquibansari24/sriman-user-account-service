package com.extrade.usermanagement.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_account")
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_account_id")
    private String userAccountId;
    @Column(name = "email_address")
    private String emailAddress;
    private String password;
    @Column(name = "first_nm")
    private String firstName;
    @Column(name = "last_nm")
    private String lastName;
    private LocalDate dob;
    private String gender;
    @Column(name = "mobile_nbr")
    private String mobileNo;
    @Column(name = "email_verification_otp_code")
    private String emailVerificationOtpCode;
    @Column(name = "mobile_nbr_verification_otp_code")
    private String mobileNoVerificationOtpCode;
    @Column(name = "email_verification_otp_code_generated_dt")
    private LocalDateTime emailVerificationOtpCodeGeneratedDate;
    @Column(name = "mobile_nbr_verification_otp_code_generated_dt")
    private LocalDateTime mobileNoVerificationOtpCodeGeneratedDate;
    @Column(name = "email_verification_status")
    private short emailVerificationStatus;
    @Column(name = "mobile_nbr_verification_status")
    private short mobileNoVerificationStatus;
    @Column(name = "registered_dt")
    private LocalDate registeredDate;
    @Column(name = "activated_dt")
    private LocalDate activatedDate;
    private String status;
    @Column(name = "store_id")
    private int storeId;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role userRole;
    @ManyToOne
    @JoinColumn(name = "user_address_id")
    private Address userAddress;

    @Column(name = "last_modified_dt")
    private LocalDateTime lastModifiedDate;
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

}
