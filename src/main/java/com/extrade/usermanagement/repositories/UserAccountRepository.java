package com.extrade.usermanagement.repositories;

import com.extrade.usermanagement.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    long countByEmailAddress(String emailAddress);

    long countByMobileNo(String mobileNo);

    @Modifying
    @Query("update user_account ua set ua.emailVerificationStatus = ?2, ua.mobileNoVerificationStatus = ?3, ua.lastModifiedDate=?4, ua.activatedDate=?5, ua.status=?6 where ua.userAccountId = ?1")
    int updateUserAccount(long userAccountId, short emailVerificationCodeVerifiedStatus,
                          short mobileVerificationCodeVerifiedStatus, LocalDate lastModifiedDate, LocalDate activatedDate,
                          String accountStatus);
}
