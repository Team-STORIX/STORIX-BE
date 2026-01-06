package com.storix.storix_api.domains.profile.controller;

import com.storix.storix_api.domains.profile.application.usecase.ProfileUseCase;
import com.storix.storix_api.domains.profile.dto.UserInfo;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
@Tag(name = "프로필", description = "프로필 관련 API")
public class ProfileController {

    private final ProfileUseCase profileUseCase;

    @Operation(summary = "기본 프로필 조회", description = "기본 프로필을 조회하는 api 입니다.")
    @GetMapping("/me")
    public ResponseEntity<CustomResponse<UserInfo>> getProfile(
            @AuthenticationPrincipal AuthUserDetails authUserDetails
    ) {
        return ResponseEntity.ok()
                .body(profileUseCase.getUserProfile(authUserDetails));
    }

}
