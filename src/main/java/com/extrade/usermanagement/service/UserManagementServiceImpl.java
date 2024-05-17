package com.extrade.usermanagement.service;

import com.extrade.connect.beans.notification.MailNotification;
import com.extrade.connect.beans.notification.Notification;
import com.extrade.connect.manager.NotificationManager;
import com.extrade.usermanagement.dto.AccountVerificationStatusDto;
import com.extrade.usermanagement.dto.UserAccountDto;
import com.extrade.usermanagement.entities.Role;
import com.extrade.usermanagement.entities.UserAccount;
import com.extrade.usermanagement.exceptions.AccountVerificationException;
import com.extrade.usermanagement.exceptions.UserAccountNotFoundException;
import com.extrade.usermanagement.exceptions.UserAlreadyActivatedException;
import com.extrade.usermanagement.exceptions.VerificationCodeMisMatchException;
import com.extrade.usermanagement.repositories.RoleRepository;
import com.extrade.usermanagement.repositories.UserAccountRepository;
import com.extrade.usermanagement.utilities.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserManagementServiceImpl implements UserManagmentService {
    private final String TMPL_VERIFY_EMAIL = "confirm-email.html";
    private final String TMPL_VERIFY_MOBILE = "confirm-mobile.html";
    private final String TMPL_CUSTOMER_WELCOME = "customer-welcome.html";

    private final UserAccountRepository userAccountRepository;
    private final NotificationManager notificationManager;
    private final RoleRepository roleRepository;
    private final String xtradeCustomerWebLink;


    public UserManagementServiceImpl(UserAccountRepository userAccountRepository,
                                     NotificationManager notificationManager,
                                     RoleRepository roleRepository,
                                     @Value("${eXtrade.customer.weblink}") String xtradeCustomerWebLink) {
        this.userAccountRepository = userAccountRepository;
        //this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.notificationManager = notificationManager;
        this.roleRepository = roleRepository;
        this.xtradeCustomerWebLink = xtradeCustomerWebLink;
    }

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
        String emailVerificationLink = null;
        MailNotification mailNotification = null;

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

        try {

            emailVerificationLink = xtradeCustomerWebLink + "/customer/" + userAccountId + "/"
                    + emailVerificationOtpCode + "/verifyEmail";
            log.debug("email verification link: {} generated", emailVerificationLink);

            Map<String, Object> tokens = new HashMap<>();
            tokens.put("user", userAccountDto.getFirstName() + " " + userAccountDto.getLastName());
            tokens.put("link", emailVerificationLink);

            mailNotification = new MailNotification();
            mailNotification.setFrom("noreply@xtrade.com");
            mailNotification.setTo(new String[]{userAccountDto.getEmailAddress()});
            mailNotification.setSubject("verify your email address");
            mailNotification.setTemplateName(TMPL_VERIFY_EMAIL);
            mailNotification.setTokens(tokens);
            mailNotification.setAttachments(Collections.emptyList());

            notificationManager.email(mailNotification);

            Notification notification = new MailNotification();
            notification.setFrom("+91-9393933");
            notification.setTo(new String[]{userAccountDto.getMobileNo()});
            notification.setTemplateName(TMPL_VERIFY_MOBILE);
            tokens = new HashMap<>();
            tokens.put("mobileOtpCode", mobileNoVerificationOtpCode);

            notificationManager.text(notification);
        } catch (Exception e) {
            log.error("error while sending the email to user :{}", userAccountDto.getEmailAddress(), e);
        }

        return userAccountId;
    }

    @Override
    @Transactional(readOnly = false)
    public AccountVerificationStatusDto verifyOtpAndUpdateAccountStatus(int userAccountId,
                                                                        String verificationCode,
                                                                        VerificationTypeEnum verificationType) {
        LocalDate now = LocalDate.now();
        UserAccount userAccount = null;
        AccountVerificationStatusDto accountVerificationStatusDto = null;
        MailNotification mailNotification = null;

        Optional<UserAccount> optionalUserAccount = userAccountRepository.findById(userAccountId);

        if (optionalUserAccount.isEmpty()) {
            throw new UserAccountNotFoundException("UserAccount with id:" + userAccountId + " does not exists to verify");
        }
        userAccount = optionalUserAccount.get();


        accountVerificationStatusDto = AccountVerificationStatusDto.of().userAccountId(userAccountId)
                .emailVerificationStatus(userAccount.getEmailVerificationStatus())
                .mobileVerificationStatus(userAccount.getMobileNoVerificationStatus())
                .accountStatus(userAccount.getStatus()).build();

        if (userAccount.getStatus().equals(UserAccountStatusEnum.ACTIVE.toString())) {
            throw new UserAlreadyActivatedException("UserAccount Id:" + userAccountId + " has already activated");
        }

        if (verificationType == VerificationTypeEnum.VERIFY_MOBILE) {

            if (userAccount.getMobileNoVerificationStatus()
                    == UserAccountConstants.OTP_STATUS_VERIFIED) {
                throw new AccountVerificationException("Mobile verification already finished", verificationType);
            }

            if (userAccount.getMobileNoVerificationOtpCode().equals(verificationCode) == false) {
                throw new VerificationCodeMisMatchException("mobile verification code mis-match", verificationType);
            }

            accountVerificationStatusDto.setMobileVerificationStatus(UserAccountConstants.OTP_STATUS_VERIFIED);
            userAccount.setMobileNoVerificationStatus(UserAccountConstants.OTP_STATUS_VERIFIED);

            if (userAccount.getEmailVerificationStatus() == UserAccountConstants.OTP_STATUS_VERIFIED) {
                accountVerificationStatusDto.setAccountStatus(UserAccountStatusEnum.ACTIVE.toString());
                userAccount.setStatus(UserAccountStatusEnum.ACTIVE.toString());
                userAccount.setActivatedDate(now);
            }
        } else if (verificationType == VerificationTypeEnum.VERIFY_EMAIL_ADDRESS) {
            if (userAccount.getEmailVerificationStatus() == UserAccountConstants.OTP_STATUS_VERIFIED) {
                throw new AccountVerificationException("email address is already verified", verificationType);
            }

            if (userAccount.getEmailVerificationOtpCode().equals(verificationCode) == false) {
                throw new VerificationCodeMisMatchException("email verification code mis-match", verificationType);
            }

            accountVerificationStatusDto.setEmailVerificationStatus(UserAccountConstants.OTP_STATUS_VERIFIED);
            userAccount.setEmailVerificationStatus(UserAccountConstants.OTP_STATUS_VERIFIED);

            if (userAccount.getMobileNoVerificationStatus() == UserAccountConstants.OTP_STATUS_VERIFIED) {
                accountVerificationStatusDto.setAccountStatus(UserAccountStatusEnum.ACTIVE.toString());
                userAccount.setStatus(UserAccountStatusEnum.ACTIVE.toString());
                userAccount.setActivatedDate(now);
            }
        }

        //here
        int records = userAccountRepository.updateUserAccount(userAccountId, userAccount.getEmailVerificationStatus()
                , userAccount.getMobileNoVerificationStatus()
                , LocalDateTime.now(), userAccount.getActivatedDate(), userAccount.getStatus());

        if (userAccount.getStatus().equals(UserAccountStatusEnum.ACTIVE.toString()) && records > 0) {
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("user", userAccount.getFirstName() + " " + userAccount.getLastName());
            tokens.put("eXtradeWebLink", xtradeCustomerWebLink);

            mailNotification = new MailNotification();
            mailNotification.setFrom("noreply@xtrade.com");
            mailNotification.setTo(new String[]{userAccount.getEmailAddress()});
            mailNotification.setSubject("Welcome to XTrade Platform");
            mailNotification.setTemplateName(TMPL_CUSTOMER_WELCOME);
            mailNotification.setTokens(tokens);
            mailNotification.setAttachments(Collections.emptyList());
        }

        return accountVerificationStatusDto;
    }
}

































