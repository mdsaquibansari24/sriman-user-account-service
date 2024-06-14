package com.extrade.usermanagement.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

@Getter
@Builder(builderMethodName = "of")
@ToString
public class ErrorMessage {
    private final String messageID;
    private final String errorCode;
    private final Date messageDateTime;
    private final String errorMessage;
    private final String originator;

}
