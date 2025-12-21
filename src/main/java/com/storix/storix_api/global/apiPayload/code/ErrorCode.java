package com.storix.storix_api.global.apiPayload.code;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {

    // Common Error
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON_ERROR_001", "잘못된 요청입니다"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "COMMON_ERROR_002", "인증이 필요합니다"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON_ERROR_003", "접근이 금지되었습니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON_ERROR_004", "요청한 자원을 찾을 수 없습니다"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON_ERROR_005", "서버 내부 오류가 발생했습니다"),

    // Token error
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH_ERROR_001", "잘못된 토큰입니다"),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_ERROR_401_001", "토큰이 만료되었습니다. 토큰을 재 발급 해주세요"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "AUTH_ERROR_403_001", "토큰이 만료되었습니다. 재로그인 해주세요"),

    // Login error
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_ERROR_002", "아이디 또는 비밀번호가 일치하지 않습니다"),

    // SignUp error
    DUPLICATE_USER_SIGN(HttpStatus.BAD_REQUEST, "AUTH_ERROR_400_001", "중복 가입 요청입니다."),

    // Other Server error
    OTHER_SERVER_BAD_REQUEST(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_1", "Other server bad request"),
    OTHER_SERVER_UNAUTHORIZED(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_2", "Other server unauthorized"),
    OTHER_SERVER_FORBIDDEN(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_3", "Other server forbidden"),
    OTHER_SERVER_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_4", "Other server expired token"),
    OTHER_SERVER_NOT_FOUND(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_5", "Other server not found error"),
    OTHER_SERVER_INTERNAL_SERVER_ERROR(HttpStatus.BAD_REQUEST, "FEIGN_ERROR_6", "Other server internal server error"),

    // OIDC error
    OIDC_OLD_PUBLIC_KEY_ERROR(HttpStatus.BAD_REQUEST, "OIDC_ERORR_1", "OIDC 공개키 갱신이 필요합니다.")
    ;


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
