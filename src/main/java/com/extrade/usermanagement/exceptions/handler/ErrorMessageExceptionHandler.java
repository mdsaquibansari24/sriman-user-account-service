package com.extrade.usermanagement.exceptions.handler;

import com.extrade.usermanagement.dto.ErrorMessage;
import com.extrade.usermanagement.dto.ErrorMessageFactory;
import com.extrade.usermanagement.exceptions.AccountVerificationException;
import com.extrade.usermanagement.exceptions.UserAccountNotFoundException;
import com.extrade.usermanagement.exceptions.UserAlreadyActivatedException;
import com.extrade.usermanagement.exceptions.VerificationCodeMisMatchException;
import com.extrade.usermanagement.utilities.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@AllArgsConstructor
public class ErrorMessageExceptionHandler {
    private final ErrorMessageFactory errorMessageFactory;


    @ExceptionHandler(UserAccountNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserAccountNotFoundException(HttpServletRequest request, UserAccountNotFoundException e) {
        log.error(request.getRequestURL().toString(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessageFactory.failure(e, ErrorCodes.USER_NOT_FOUND));
    }

    @ExceptionHandler(UserAlreadyActivatedException.class)
    public ResponseEntity<ErrorMessage> handleUserAlreadyActivatedException(HttpServletRequest request, UserAlreadyActivatedException e) {
        log.error(request.getRequestURL().toString(), e);
        return ResponseEntity.status(HttpStatus.GONE).body(errorMessageFactory.failure(e, ErrorCodes.USER_ALREADY_ACTIVATED));
    }

    @ExceptionHandler(AccountVerificationException.class)
    public ResponseEntity<ErrorMessage> handleAccountVerificationException(HttpServletRequest request, AccountVerificationException e) {
        log.error(request.getRequestURL().toString(), e);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorMessageFactory.failure(e, ErrorCodes.OTPCODE_ALREADY_VERIFIED));
    }

    @ExceptionHandler(VerificationCodeMisMatchException.class)
    public ResponseEntity<ErrorMessage> handleVerificationCodeMisMatchException(HttpServletRequest request, VerificationCodeMisMatchException e) {
        log.error(request.getRequestURL().toString(), e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessageFactory.failure(e, ErrorCodes.VERIFICATION_CODE_MISMATCH));
    }
}