package com.storix.storix_api.domains.user.application;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.ArtistLoginRequest;
import com.storix.storix_api.controller.auth.dto.LoginWithTokenResponse;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.TokenGenerateHelper;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class LoginUseCase {

    private final ArtistLoginService artistLoginService;
    private final TokenGenerateHelper tokenGenerateHelper;

    /**UserDetails -> AuthUserDetails: (String)userId, (String)role */

    /**
     * 독자용
     * username =
     *
     * id: User PK
     * */


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
