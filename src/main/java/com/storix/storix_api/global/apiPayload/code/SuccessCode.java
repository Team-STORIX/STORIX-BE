package com.storix.storix_api.global.apiPayload.code;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum SuccessCode {

    SUCCESS(HttpStatus.OK, "COMMON_SUCCESS_001", "정상적인 요청입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_SUCCESS_002", "정상적으로 생성되었습니다."),

    // login success
    VALID_LOGIN(HttpStatus.OK, "AUTH_SUCCESS_001", "로그인에 성공했습니다."),
    VALID_LOGOUT(HttpStatus.OK, "AUTH_SUCCESS_002", "로그아웃에 성공했습니다."),

    // authorization success
    SUCCESS_REISSUE_ACCESSTOKEN(HttpStatus.CREATED, "AUTHORIZATION_SUCCESS_001", "엑세스 토큰 재발급에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
