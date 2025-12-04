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
            // case NAVER
            // return authService.validNaverSignup(req);
            default -> {return null;}
        }
    }

    // 독자 유저 정보 등록
    public CustomResponse<LoginWithTokenResponse> readerSignup(ReaderSignupRequest req) {
        AuthUserDetails userDetails = authService.signUpReaderUser(req);
        LoginWithTokenResponse loginWithTokenResponse = tokenGenerateHelper.generateLoginWithToken(userDetails);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, loginWithTokenResponse);
    }

    // 작가 회원 가입
    public CustomResponse<ArtistSignupResponse> artistSignup(ArtistSignupRequest req) {
        Long artistUserId = authService.signUpArtistUser(req);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS,
                new ArtistSignupResponse(artistUserId, req.loginId(), req.nickName()));
    }
}
