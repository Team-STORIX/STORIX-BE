package com.storix.storix_api.global.apiPayload.exception;

import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ErrorResponse (
        Boolean isSuccess,
        String code,
        String message,
        LocalDateTime timestamp
) {

    public ErrorResponse(ErrorCode errorCode) {
        this(false, errorCode.getCode(), errorCode.getMessage(), LocalDateTime.now());
    }

}