package com.storix.storix_api.domains.preference.controller;

import com.storix.storix_api.domains.preference.application.usecase.ExplorationUseCase;
import com.storix.storix_api.domains.preference.dto.ExplorationResultResponseDto;
import com.storix.storix_api.domains.preference.dto.ExplorationSubmitRequestDto;
import com.storix.storix_api.domains.preference.dto.ExplorationWorksResponseDto;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/preference")
public class PreferenceController {

    private final ExplorationUseCase explorationUseCase;

    @GetMapping("/exploration")
    public CustomResponse<List<ExplorationWorksResponseDto>> getExploration(
            @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                explorationUseCase.getExplorationWorks(authUserDetails.getUserId())
        );

    }

    @PostMapping("/exploration")
    public CustomResponse<String> submitResponse(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @RequestBody ExplorationSubmitRequestDto request
    ) {
        explorationUseCase.submitExploration(authUserDetails.getUserId(), request);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS);
    }

    @GetMapping("/results")
    public CustomResponse<ExplorationResultResponseDto> getResults(
            @AuthenticationPrincipal AuthUserDetails authUserDetails) {
        return CustomResponse.onSuccess(SuccessCode.SUCCESS,
                explorationUseCase.getExplorationResults(authUserDetails.getUserId()));
    }
}