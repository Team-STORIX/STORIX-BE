package com.storix.storix_api.domains.user.application;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.controller.auth.dto.ArtistSignupRequest;
import com.storix.storix_api.controller.auth.dto.ArtistSignupResponse;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class AuthUseCase {

    private final AuthService authService;

    public CustomResponse<ArtistSignupResponse> artistSignup(ArtistSignupRequest req) {
        Long artistUserId = authService.signUpArtistUser(req);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS,
                new ArtistSignupResponse(artistUserId, req.loginId(), req.nickName()));
    }
}
