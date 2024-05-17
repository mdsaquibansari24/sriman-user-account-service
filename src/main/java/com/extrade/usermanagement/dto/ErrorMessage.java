package com.extrade.usermanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Builder(builderMethodName = "of")
@ToString
public class ErrorMessage {
    private final String messageID;
    private final String errorCode;
    private final LocalDateTime messageDateTime;
    private final String errorMessage;
    private final String originator;

}
