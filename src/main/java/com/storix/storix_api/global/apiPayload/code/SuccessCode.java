package com.storix.storix_api.global.apiPayload.code;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum SuccessCode {

    SUCCESS(HttpStatus.OK, "COMMON_SUCCESS_001", "정상적인 요청입니다."),
    CREATED(HttpStatus.CREATED, "COMMON_SUCCESS_002", "정상적으로 생성되었습니다."),

    // Notification success
    NOTIFICATION_LOAD_SUCCESS(HttpStatus.OK, "NOTI2001", "알림 목록을 성공적으로 조회했습니다."),
    NOTIFICATION_COUNT_SUCCESS(HttpStatus.OK, "NOTI2002", "안 읽은 알림 개수를 성공적으로 조회했습니다."),
    NOTIFICATION_READ_SUCCESS(HttpStatus.OK, "NOTI2003", "알림을 읽음 처리했습니다."),
    NOTIFICATION_READ_ALL_SUCCESS(HttpStatus.OK, "NOTI2004", "모든 알림을 읽음 처리했습니다."),

    // login success
    VALID_LOGIN(HttpStatus.OK, "AUTH_SUCCESS_001", "로그인에 성공했습니다."),
    VALID_LOGOUT(HttpStatus.OK, "AUTH_SUCCESS_002", "로그아웃에 성공했습니다."),
    VALID_SOCIAL_LOGIN(HttpStatus.OK, "AUTH_SUCCESS_003", "회원가입이 필요합니다."),

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
