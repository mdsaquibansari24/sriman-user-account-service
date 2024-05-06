package com.extrade.usermanagement.service;

import com.extrade.usermanagement.dto.UserAccountDto;
import com.extrade.usermanagement.entities.Role;
import com.extrade.usermanagement.entities.UserAccount;
import com.extrade.usermanagement.repositories.RoleRepository;
import com.extrade.usermanagement.repositories.UserAccountRepository;
import com.extrade.usermanagement.utilities.RandomGenerator;
import com.extrade.usermanagement.utilities.RoleCodeEnum;
import com.extrade.usermanagement.utilities.UserAccountConstants;
import com.extrade.usermanagement.utilities.UserAccountStatusEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserManagementServiceImpl implements UserManagmentService {
    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional(readOnly = true)
    public long countUsersByEmailAddress(String emailAddress) {
        return userAccountRepository.countByEmailAddress(emailAddress);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsersByMobileNo(String mobileNo) {
        return userAccountRepository.countByMobileNo(mobileNo);
    }

    @Override
    @Transactional(readOnly = false)
    public long registerCustomer(UserAccountDto userAccountDto) {
        Role userRole = null;
        LocalDateTime time = null;
        long userAccountId = 0;
        UserAccount userAccount = null;
        String emailVerificationOtpCode = null;
        String mobileNoVerificationOtpCode = null;

        time = LocalDateTime.now();
        emailVerificationOtpCode = RandomGenerator.randomAlphaNumericSpecialCharsSequence(8);
        mobileNoVerificationOtpCode = RandomGenerator.randomNumericSequence(6);

        userRole = roleRepository.findByRoleCode(RoleCodeEnum.CUSTOMER.toString());
        log.info("fetched the user role id: {} for the role_cd: {}", userRole.getRoleId(), RoleCodeEnum.CUSTOMER.toString());

        userAccount = new UserAccount();
        userAccount.setFirstName(userAccountDto.getFirstName());
        userAccount.setLastName(userAccountDto.getLastName());
        userAccount.setEmailAddress(userAccountDto.getEmailAddress());
        userAccount.setMobileNo(userAccountDto.getMobileNo());
        userAccount.setPassword(userAccountDto.getPassword());
        userAccount.setGender(userAccountDto.getGender());
        userAccount.setDob(userAccountDto.getDob());
        userAccount.setEmailVerificationOtpCode(emailVerificationOtpCode);
        userAccount.setMobileNoVerificationOtpCode(mobileNoVerificationOtpCode);
        userAccount.setEmailVerificationOtpCodeGeneratedDate(time);
        userAccount.setMobileNoVerificationOtpCodeGeneratedDate(time);
        userAccount.setUserRole(userRole);
        userAccount.setRegisteredDate(LocalDate.now());
        userAccount.setEmailVerificationStatus((short) 0);
        userAccount.setMobileNoVerificationStatus((short) 0);
        userAccount.setLastModifiedBy(UserAccountConstants.SYSTEM_USER);
        userAccount.setLastModifiedDate(time);
        userAccount.setStatus(UserAccountStatusEnum.REGISTERED.getName());

        userAccountId = userAccountRepository.save(userAccount).getUserAccountId();
        log.info("userAccount of email: {} has been saved with userAccountId:{}", userAccount.getEmailAddress(), userAccountId);


        return userAccountId;
    }
}
