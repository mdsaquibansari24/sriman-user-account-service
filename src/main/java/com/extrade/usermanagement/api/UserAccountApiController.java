package com.extrade.usermanagement.api;

import com.extrade.usermanagement.service.UserManagmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class UserAccountApiController {
    private final UserManagmentService userManagmentService;

    @GetMapping(value = "/count/email")
    public ResponseEntity<Long> getNoUsersByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        long c = 0;
        c = userManagmentService.countUsersByEmailAddress(emailAddress);
        log.info("no of users found are: {} for the emailAddress: {}", c, emailAddress);
        return ResponseEntity.ok(c);
    }

    @GetMapping(value = "/count/mobileNo")
    public ResponseEntity<Long> getNoOfUsersByMobileNo(@RequestParam("mobileNo") String mobileNo) {
        long c = 0;
        c = userManagmentService.countUsersByMobileNo(mobileNo);
        log.info("no of users found are: {} for the mobileNo: {}", c, mobileNo);
        return ResponseEntity.ok(c);
    }
}
