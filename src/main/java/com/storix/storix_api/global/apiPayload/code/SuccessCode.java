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

    // Auth success
    OAUTH_LOGIN_SUCCESS(HttpStatus.OK, "OAUTH_SUCCESS_001", "소셜 로그인에 성공했습니다."),
    OAUTH_PRE_LOGIN_SUCCESS(HttpStatus.OK, "OAUTH_SUCCESS_002", "소셜 로그인에 성공했습니다. 회원가입이 필요합니다."),
    AUTH_NICKNAME_SUCCESS(HttpStatus.OK, "NICKNAME_SUCCESS_001", "사용 가능한 닉네임입니다."),
    AUTH_SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH_SUCCESS_001", "유저 정보 등록이 완료되었습니다."),
    AUTH_LOGOUT_SUCCESS(HttpStatus.OK, "AUTH_SUCCESS_002", "로그아웃에 성공했습니다."),
    AUTH_REISSUE_ACCESSTOKEN_SUCCESS(HttpStatus.CREATED, "AUTH_SUCCESS_003", "엑세스 토큰 재발급에 성공했습니다."),
    AUTH_ARTIST_SIGNUP_SUCCESS(HttpStatus.CREATED, "AUTH_SUCCESS_004", "작가 계정 생성이 완료되었습니다."),
    AUTH_ARTIST_LOGIN_SUCCESS(HttpStatus.OK, "AUTH_SUCCESS_005", "작가 계정 로그인에 성공했습니다."),
    AUTH_WITHDRAW_SUCCESS(HttpStatus.OK, "AUTH_SUCCESS_006", "회원 탈퇴에 성공했습니다."),

    // Profile success
    PROFILE_LOAD_SUCCESS(HttpStatus.OK, "PROFILE_SUCCESS_001", "유저 정보 조회에 성공했습니다."),
    PROFILE_NICKNAME_SUCCESS(HttpStatus.OK, "PROFILE_SUCCESS_002", "변경 가능한 닉네임입니다."),
    PROFILE_UPDATE_NICKNAME_SUCCESS(HttpStatus.OK, "PROFILE_SUCCESS_003", "닉네임 변경에 성공했습니다."),
    PROFILE_UPDATE_DESCRIPTION_SUCCESS(HttpStatus.OK, "PROFILE_SUCCESS_004", "한 줄 소개 변경에 성공했습니다."),
    PROFILE_UPDATE_IMAGE_SUCCESS(HttpStatus.OK, "PROFILE_SUCCESS_005", "프로필 이미지 변경에 성공했습니다."),

    // Image success
    IMAGE_ISSUE_PRESIGNED_URL_SUCCESS(HttpStatus.OK, "IMAGE_SUCCESS_001", "이미지를 업로드할 Presigned Url 발급에 성공했습니다."),

    // Plus success
    PLUS_BOARD_UPLOAD_SUCCESS(HttpStatus.CREATED, "PLUS_SUCCESS_001", "게시물 등록에 성공했습니다."),
    PLUS_REVIEW_UPLOAD_SUCCESS(HttpStatus.CREATED,"PLUS_SUCCESS_002", "리뷰 등록에 성공했습니다."),
    PLUS_WORKS_LOAD_SUCCESS(HttpStatus.OK, "PLUS_SUCCESS_003", "작품 정보 조회에 성공했습니다."),
    PLUS_REVIEW_CHECK_SUCCESS(HttpStatus.OK, "PLUS_SUCCESS_004", "리뷰 작성이 가능합니다."),


    // Library success
    LIBRARY_WORKS_LOAD_SUCCESS(HttpStatus.OK, "LIBRARY_SUCCESS_001", "서재 정보 조회에 성공했습니다."),
    LIBRARY_SEARCH_SUCCESS(HttpStatus.OK, "LIBRARY_SUCCESS_002", "서재 내 검색에 성공했습니다."),
    LIBRARY_RECENT_LOAD_SUCCESS(HttpStatus.OK, "LIBRARY_SUCCESS_003", "서재 최근 검색어 조회에 성공했습니다."),
    LIBRARY_RECENT_REMOVE_SUCCESS(HttpStatus.OK, "LIBRARY_SUCCESS_004", "서재 최근 검색어 삭제에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SuccessCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
