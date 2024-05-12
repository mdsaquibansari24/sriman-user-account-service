package com.extrade.usermanagement.service;


import com.extrade.connect.beans.notification.MailNotification;
import com.extrade.connect.beans.notification.Notification;
import com.extrade.connect.manager.NotificationManager;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service

@Slf4j
public class UserManagementServiceImpl implements UserManagmentService {
    private final String TEMP_VERIFY_EMAIL="emai-verification.html";
    private final String TEMP_VERIFY_MOBILE="mobile-verification.html";



    private final UserAccountRepository userAccountRepository;
    private final RoleRepository roleRepository;
    private final String xtradeCustomerWebLink;
    private final NotificationManager notificationManager;
    public UserManagementServiceImpl(UserAccountRepository userAccountRepository, RoleRepository roleRepository, @Value("${eXtrade.customer.weblink}") String xtradeCustomerWebLink, NotificationManager notificationManager) {
        this.userAccountRepository = userAccountRepository;
        this.roleRepository = roleRepository;
        this.xtradeCustomerWebLink = xtradeCustomerWebLink;
        this.notificationManager = notificationManager;
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
    @Transactional(readOnly=false)
    public long registerCustomer(UserAccountDto userAccountDto) {
        Long userAccountId=null;
        Role userRole=null;
        UserAccount userAccount=null;
        RandomGenerator randomGenerator=null;
        String emailVerificationLink=null;
        MailNotification mailNotification=null;//creating mail notifiction object
        LocalDateTime time=null;
        String emailVerificationOtpCode=null;
        String mobileNoVerificationOtpCode=null;
        time=LocalDateTime.now();

        userRole=roleRepository.findByRoleCode(RoleCodeEnum.CUSTOMER.toString());
        log.info("fetching user roleId: {} with user role code: {}",userRole.getRoleId(),RoleCodeEnum.CUSTOMER.toString());

        randomGenerator=new RandomGenerator();
        emailVerificationOtpCode=randomGenerator.emailAddressVerificationOtpGenerator(6);
        mobileNoVerificationOtpCode=randomGenerator.mobileNoVerificationOtpGenerator(6);


        userAccount=new UserAccount();
        userAccount.setUserAccountId(userAccountDto.getUserAccountId());
        userAccount.setFirstName(userAccountDto.getFirstName());
        userAccount.setLastName(userAccountDto.getLastName());
        userAccount.setEmailAddress(userAccountDto.getEmailAddress());
        userAccount.setMobileNo(userAccountDto.getMobileNo());
        userAccount.setDob(userAccountDto.getDob());
        userAccount.setGender(userAccountDto.getGender());
        userAccount.setPassword(userAccountDto.getPassword());
        userAccount.setEmailVerificationOtpCode(emailVerificationOtpCode);
        userAccount.setMobileNoVerificationOtpCode(mobileNoVerificationOtpCode);
        userAccount.setEmailVerificationOtpCodeGeneratedDate(time);
        userAccount.setMobileNoVerificationOtpCodeGeneratedDate(time);
        userAccount.setRegisteredDate(LocalDate.now());
        userAccount.setUserRole(userRole);
        userAccount.setMobileNoVerificationStatus((short) 0);
        userAccount.setEmailVerificationStatus((short) 0);
        userAccount.setLastModifiedDate(time);
        userAccount.setLastModifiedBy(UserAccountConstants.SYSTEM_USER);
        userAccount.setStatus(UserAccountStatusEnum.REGISTERED.toString());

        userAccountId=userAccountRepository.save(userAccount).getUserAccountId();
        log.info("userAccount with email: {} saved with userAccountId: {}",userAccount.getEmailAddress(),userAccountId);

        try {
            // generating link to be sent to user email address for verification
            emailVerificationLink = xtradeCustomerWebLink + "/customer/" + userAccountId + "/"
                    + emailVerificationOtpCode + "/verifyEmail";
            log.info("email verification link: {} generated", emailVerificationLink);

            mailNotification = new MailNotification();
            mailNotification.setFrom("noreply@xtrade.com");
            mailNotification.setTo(new String[]{userAccountDto.getEmailAddress()});
            mailNotification.setSubject("verify your email address");
            mailNotification.setTemplateName(TEMP_VERIFY_EMAIL);
            log.info("getting templatename: {}",mailNotification.getTemplateName());

            //setting tokens
            Map<String, Object> tokens = new HashMap<>();
            tokens.put("user", userAccountDto.getFirstName() + " " + userAccountDto.getLastName());
            tokens.put("link", emailVerificationLink);

            mailNotification.setTokens(tokens);
            mailNotification.setAttachments(Collections.emptyList());
            //sending mail
            notificationManager.email(mailNotification);
            log.info("mail sent successfully to: {}",userAccountDto.getEmailAddress());

            //sending otp to mobile number
            Notification notification=new MailNotification();
            notification.setFrom("+93-0011223");
            notification.setTo(new String[]{userAccountDto.getMobileNo()});
            notification.setTemplateName(TEMP_VERIFY_MOBILE);
            //setting tokens
            tokens=new HashMap<>();
            tokens.put("user",userAccountDto.getFirstName());
            tokens.put("otp",mobileNoVerificationOtpCode);
            notification.setTokens(tokens);
            log.info("otp sent successfully to: {}",userAccountDto.getMobileNo());

            //sending otp
            notificationManager.text(notification);



        }catch (Exception e){
            log.error("error while sending the email to user: {} error: {}",userAccountDto.getEmailAddress(),e);
        }

        return userAccountId;
    }


}

