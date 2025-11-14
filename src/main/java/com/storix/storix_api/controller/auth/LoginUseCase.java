package com.storix.storix_api.controller.auth;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.dto.ArtistLoginRequest;
import com.storix.storix_api.controller.dto.LoginWithTokenResponse;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.TokenGenerateHelper;
import com.storix.storix_api.domains.user.service.ArtistLoginService;
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
     * username = String.valueOf(id)
     *
     * id: User PK
     * */


    /**
     * 작가용
     * username = loginId // TODO: String.valueOf(id)로 바꿀 것
     *
     * @param req
     * */
    public CustomResponse<LoginWithTokenResponse> artistLoginWithLoginId(ArtistLoginRequest req) {
        if (artistLoginService.artistLogin(req.loginId(), req.password())){
            AuthUserDetails userDetails = artistLoginService.loadUserByUsername(req.loginId());
            LoginWithTokenResponse loginWithTokenResponse = tokenGenerateHelper.generateLoginWithToken(userDetails);
            return CustomResponse.onSuccess(SuccessCode.SUCCESS, loginWithTokenResponse);
        }
        return null; // TODO: Exception 핸들링
    }
}
