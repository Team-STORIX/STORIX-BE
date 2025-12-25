package com.storix.storix_api.domains.user.application.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.*;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.application.usecase.helper.TokenGenerateHelper;
import com.storix.storix_api.domains.user.application.service.AuthService;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthService authService;
    private final TokenGenerateHelper tokenGenerateHelper;

    // 독자 회원 가입 가능 여부
    public ValidAuthDTO checkAvailableRegister(OAuthAuthorizationRequest req, OAuthProvider provider) {
        switch (provider) {
            case KAKAO -> {
                return authService.validKakaoSignup(req);
            }
            case NAVER -> {
                return authService.validNaverSignup(req);
            }
            default -> {return null;}
        }
    }

    // 독자 유저 정보 등록
    public CustomResponse<LoginWithTokenResponse> readerSignup(ReaderSignupRequest req, String jti) {
        AuthUserDetails userDetails = authService.signUpReaderUser(req, jti);
        LoginWithTokenResponse loginWithTokenResponse = tokenGenerateHelper.generateLoginWithToken(userDetails);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, loginWithTokenResponse);
    }

    // 닉네임 중복 체크
    public CustomResponse<Void> checkAvailableNickname(String nickName) {
        authService.validNickname(nickName);
        return CustomResponse.onSuccess(SuccessCode.VALID_NICKNAME);
    }

    // 작가 회원 가입
    public CustomResponse<ArtistSignupResponse> artistSignup(ArtistSignupRequest req) {
        Long artistUserId = authService.signUpArtistUser(req);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS,
                new ArtistSignupResponse(artistUserId, req.loginId(), req.nickName()));
    }
}
