package com.storix.storix_api.global.apiPayload.code;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum SuccessCode {

    SUCCESS(HttpStatus.OK, "COMMON_SUCCESS_001", "정상적인 요청입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_SUCCESS_002", "정상적으로 생성되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
