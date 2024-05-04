package com.extrade.usermanagement.service;

import com.extrade.usermanagement.dto.UserAccountDto;
import com.extrade.usermanagement.entities.UserAccount;
import com.extrade.usermanagement.repositories.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagmentService {
    private final UserAccountRepository userAccountRepository;

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
    public long registerCustomer(UserAccountDto userAccountDto) {
        UserAccount userAccount = null;



        return 0;
    }
}
