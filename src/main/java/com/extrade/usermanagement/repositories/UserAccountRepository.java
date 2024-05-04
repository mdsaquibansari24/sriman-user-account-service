package com.extrade.usermanagement.repositories;

import com.extrade.usermanagement.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    long countByEmailAddress(String emailAddress);

    long countByMobileNo(String mobileNo);
}
