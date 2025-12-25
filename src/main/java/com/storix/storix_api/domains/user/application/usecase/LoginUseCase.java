package com.storix.storix_api.domains.user.application.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.*;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.application.service.LogoutService;
import com.storix.storix_api.domains.user.application.usecase.helper.TokenGenerateHelper;
import com.storix.storix_api.domains.user.application.service.ArtistLoginService;
import com.storix.storix_api.domains.user.application.service.ReaderLoginService;
import com.storix.storix_api.domains.user.domain.OAuthInfo;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {

    private final ReaderLoginService readerLoginService;
    private final ArtistLoginService artistLoginService;
    private final LogoutService logoutService;
    private final TokenGenerateHelper tokenGenerateHelper;

    /**UserDetails -> AuthUserDetails: (String)userId, (String)role */

    /**
     * 독자용
     * */
    // 회원가입한 경우 로그인 처리
    public CustomResponse<ReaderSocialLoginResponse> readerLoginWithIdToken(String idToken, OAuthProvider provider) {
        AuthUserDetails userDetails = readerLoginService.execute(idToken, provider);
        LoginWithTokenResponse loginToken = tokenGenerateHelper.generateLoginWithToken(userDetails);

        ReaderLoginResponse readerLoginResponse = new ReaderLoginResponse(
                loginToken.accessToken(),
                loginToken.refreshToken()
        );

        return CustomResponse.onSuccess(SuccessCode.VALID_LOGIN, new ReaderSocialLoginResponse(true, readerLoginResponse, null));
    }

    // 회원가입하지 않은 경우 로그인 처리
    public CustomResponse<ReaderSocialLoginResponse> readerPreLoginWithIdToken(String idToken, OAuthProvider provider) {
        OAuthInfo oauthInfo = readerLoginService.getOauthInfoByIdToken(idToken, provider);

        OAuthLoginWithTokenResponse onboardingToken = tokenGenerateHelper.generateOAuthLoginWithToken(oauthInfo);

        ReaderPreLoginResponse readerPreLoginResponse = new ReaderPreLoginResponse(
                onboardingToken.onboardingToken()
        );

        return CustomResponse.onSuccess(SuccessCode.VALID_SOCIAL_LOGIN, new ReaderSocialLoginResponse(false, null, readerPreLoginResponse));
    }

    /**
     * 작가용
     * username = loginId
     * */
    public CustomResponse<LoginWithTokenResponse> artistLoginWithLoginId(ArtistLoginRequest req) {
        artistLoginService.validateArtistLogin(req.loginId(), req.password());
        AuthUserDetails userDetails = artistLoginService.loadUserByUsername(req.loginId());
        LoginWithTokenResponse loginWithTokenResponse = tokenGenerateHelper.generateLoginWithToken(userDetails);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, loginWithTokenResponse);
    }

    // 로그아웃
    public CustomResponse<Void> userLogoutWithRefreshToken(LogoutRequest req) {
        logoutService.logoutByRefreshToken(req.refreshToken());
        return CustomResponse.onSuccess(SuccessCode.VALID_LOGOUT);
    }
}
