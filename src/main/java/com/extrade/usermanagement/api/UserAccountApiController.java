package com.extrade.usermanagement.api;

import com.extrade.usermanagement.dto.AccountVerificationStatusDto;
import com.extrade.usermanagement.dto.ErrorMessage;
import com.extrade.usermanagement.dto.UserAccountDto;
import com.extrade.usermanagement.service.UserManagmentService;
import com.extrade.usermanagement.utilities.VerificationTypeEnum;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Slf4j
public class UserAccountApiController {
    private final UserManagmentService userManagmentService;

    @PostMapping(value = "/customer", consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.TEXT_PLAIN_VALUE})
    @ApiResponses(value = {@ApiResponse(responseCode = "500",
            content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = Long.class))})})
    public ResponseEntity<String> registerCustomer(@RequestBody UserAccountDto userAccountDto) {
        long userAccountId = 0;

        userAccountId = userManagmentService.registerCustomer(userAccountDto);
        log.info("registered a new customer with id: {}", userAccountId);

        return ResponseEntity.ok(String.valueOf(userAccountId));
    }

    @GetMapping(value = "/count/email")
    @ApiResponses(value = {@ApiResponse(responseCode = "500",
            content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "*/*", schema = @Schema(implementation = Long.class))})})
    public ResponseEntity<Long> getNoUsersByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        long c = 0;
        c = userManagmentService.countUsersByEmailAddress(emailAddress);
        log.info("no of users found are: {} for the emailAddress: {}", c, emailAddress);
        return ResponseEntity.ok(c);
    }

    @GetMapping(value = "/count/mobileNo")
    @ApiResponses(value = {@ApiResponse(responseCode = "500",
            content = {@Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "200",
                    content = {@Content(mediaType = "*/*", schema = @Schema(implementation = Long.class))})})
    public ResponseEntity<Long> getNoOfUsersByMobileNo(@RequestParam("mobileNo") String mobileNo) {
        long c = 0;
        c = userManagmentService.countUsersByMobileNo(mobileNo);
        log.info("no of users found are: {} for the mobileNo: {}", c, mobileNo);
        return ResponseEntity.ok(c);
    }

    @PutMapping(value = "/{userAccountId}/{otpCode}/{verificationType}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount NotFound for verification"),
            @ApiResponse(responseCode = "410", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount already activated"),
            @ApiResponse(responseCode = "400", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "VerificationCode Mis-Match"),
            @ApiResponse(responseCode = "422", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "Otp Already Verified"),
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountVerificationStatusDto.class))}, description = "OTP Verified")})
    public AccountVerificationStatusDto verifyOtpCode(@PathVariable("userAccountId") int userAccountId,
                                                      @PathVariable("otpCode") String verificationCode,
                                                      @PathVariable("verificationType") VerificationTypeEnum verificationType) {
        AccountVerificationStatusDto verificationStatusDto = null;

        verificationStatusDto = userManagmentService.verifyOtpAndUpdateAccountStatus(userAccountId, verificationCode, verificationType);

        return verificationStatusDto;
    }

    @GetMapping(value = "/{userAccountId}/verificationStatus", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount NotFound for verification status"),
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = AccountVerificationStatusDto.class))}, description = "OTP Verified")})
    public AccountVerificationStatusDto getUserAccountVerificationStatus(@PathVariable("userAccountId") int userAccountId) {
        return userManagmentService.accountVerificationStatus(userAccountId);
    }


    @GetMapping(value = "/details", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiResponses(value = {@ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount NotFound"),
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserAccountDto.class))}, description = "User Account Information")})
    public UserAccountDto getUserAccountByEmailAddress(@RequestParam("emailAddress") String emailAddress) {
        return userManagmentService.getUserAccountByEmailAddress(emailAddress);
    }

    @PutMapping(value = "/{userAccountId}/resendMobileOTP")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount NotFound"),
            @ApiResponse(responseCode = "200", description = "Mobile OTP Code Resent")})
    public ResponseEntity<Void> resendMobileOTPCode(@PathVariable("userAccountId") int userAccountId) {
        userManagmentService.resendMobileOTPCode(userAccountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{userAccountId}/resendEmailVerificationLink")
    @ApiResponses(value = {@ApiResponse(responseCode = "404", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))}, description = "UserAccount NotFound"),
            @ApiResponse(responseCode = "200", description = "Email Verification Link Resent")})
    public ResponseEntity<Void> resendEmailVerificationLink(@PathVariable("userAccountId") int userAccountId) {
        userManagmentService.resendVerificationEmail(userAccountId);
        return ResponseEntity.ok().build();
    }
}
























