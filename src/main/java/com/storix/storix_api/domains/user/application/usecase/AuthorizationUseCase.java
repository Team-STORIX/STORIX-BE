package com.storix.storix_api.domains.user.application.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.AuthorizationResponse;
import com.storix.storix_api.controller.auth.dto.RefreshTokenRequest;
import com.storix.storix_api.domains.user.application.usecase.helper.TokenGenerateHelper;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class AuthorizationUseCase {

    private final TokenGenerateHelper tokenGenerateHelper;

    public CustomResponse<AuthorizationResponse> getAccessTokenWithRefreshToken(RefreshTokenRequest req) {
        String accessToken = tokenGenerateHelper.reissueAccessTokenWithRefreshToken(req.refreshToken());
        AuthorizationResponse result = new AuthorizationResponse(accessToken);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS_REISSUE_ACCESSTOKEN, result);
    }
}