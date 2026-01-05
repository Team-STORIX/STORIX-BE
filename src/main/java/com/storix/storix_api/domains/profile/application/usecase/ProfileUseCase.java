package com.storix.storix_api.domains.profile.application.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.domains.profile.application.service.ProfileService;
import com.storix.storix_api.domains.profile.dto.UserInfo;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.domain.Role;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

@UseCase
@RequiredArgsConstructor
public class ProfileUseCase {

    private final ProfileService profileService;

    public CustomResponse<UserInfo> getUserProfile(AuthUserDetails authUserDetails) {

        String role = authUserDetails.getRole();
        Long userId = authUserDetails.getUserId();

        if (role.equals(String.valueOf(Role.READER))) {
            UserInfo readerProfileInfo = profileService.getReaderProfileInfo(userId);
            return CustomResponse.onSuccess(SuccessCode.PROFILE_LOAD_SUCCESS, readerProfileInfo);
        } else {
            UserInfo artistProfileInfo = profileService.getArtistProfileInfo(userId);
            return CustomResponse.onSuccess(SuccessCode.PROFILE_LOAD_SUCCESS, artistProfileInfo);
        }
    }

}
