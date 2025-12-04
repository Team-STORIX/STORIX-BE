package com.storix.storix_api.domains.user.application.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.*;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
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
    private final TokenGenerateHelper tokenGenerateHelper;

    /**UserDetails -> AuthUserDetails: (String)userId, (String)role */

    /**
     * 독자용
     * */
    // 회원가입한 경우 로그인 처리
    public CustomResponse<ReaderLoginResponse> readerLoginWithIdToken(String idToken, OAuthProvider provider) {
        AuthUserDetails userDetails = readerLoginService.execute(idToken, provider);
        LoginWithTokenResponse loginToken = tokenGenerateHelper.generateLoginWithToken(userDetails);

        ReaderLoginResponse readerLoginResponse = ReaderLoginResponse.of(
                loginToken.accessToken(),
                loginToken.refreshToken()
        );

        return CustomResponse.onSuccess(SuccessCode.SUCCESS, readerLoginResponse);
    }

    // 회원가입하지 않은 경우 로그인 처리
    public CustomResponse<ReaderPreLoginResponse> readerPreLoginWithIdToken(String idToken, OAuthProvider provider) {
        OAuthInfo oauthInfo = readerLoginService.getOauthInfoByIdToken(idToken, provider);

        ReaderPreLoginResponse readerPreLoginResponse = ReaderPreLoginResponse.of(
                oauthInfo.getProvider(),
                oauthInfo.getOid()
        );

        return CustomResponse.onSuccess(SuccessCode.SUCCESS, readerPreLoginResponse);
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
}
