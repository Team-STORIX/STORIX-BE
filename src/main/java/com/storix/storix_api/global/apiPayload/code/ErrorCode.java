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
    ONBOARDING_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH_ERROR_401_002", "온보딩 토큰이 만료되었습니다. 소셜 로그인 재시도 해주세요"),

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

    // Kakao OAuth error
    KOE009(HttpStatus.BAD_REQUEST, "KAKAO_KOE009", "등록되지 않은 플랫폼에서 액세스 토큰을 요청 하는 경우"),
    KOE010(HttpStatus.BAD_REQUEST, "KAKAO_KOE101", "클라이언트 시크릿(Client secret) 기능을 사용하는 앱에서 토큰 요청 시 client_secret 값을 전달하지 않거나 정확하지 않은 값을 전달하는 경우"),
    KOE303(HttpStatus.BAD_REQUEST, "KAKAO_KOE303", "인가 코드 요청 시 사용한 redirect_uri와 액세스 토큰 요청 시 사용한 redirect_uri가 다릅니다"),
    KOE320(HttpStatus.BAD_REQUEST, "KAKAO_KOE320", "동일한 인가 코드를 두 번 이상 사용하거나, 이미 만료된 인가 코드를 사용한 경우, 혹은 인가 코드를 찾을 수 없는 경우입니다."),
    KOE_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "KAKAO_KOE_INVALID_REQUEST","잘못된 요청인 경우"),

    // OIDC error
    OIDC_OLD_PUBLIC_KEY_ERROR(HttpStatus.BAD_REQUEST, "OIDC_ERORR_1", "OIDC 공개키 갱신이 필요합니다."),

    // Notification error
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "NOTIFICATION_ERROR_001", "알림을 찾을 수 없습니다"),
    NOTIFICATION_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "NOTIFICATION_ERROR_002", "인가되지 않은 접근입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
