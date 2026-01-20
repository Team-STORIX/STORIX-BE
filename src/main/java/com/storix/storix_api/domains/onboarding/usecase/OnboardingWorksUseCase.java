package com.storix.storix_api.domains.onboarding.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.domains.onboarding.adaptor.OnboardingWorksAdaptor;
import com.storix.storix_api.domains.onboarding.dto.OnboardingWorksInfo;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCase
@RequiredArgsConstructor
public class OnboardingWorksUseCase {

    private final OnboardingWorksAdaptor onboardingWorksAdaptor;

    public CustomResponse<List<OnboardingWorksInfo>> findAllOnboardingWorks() {
        List<OnboardingWorksInfo> result = onboardingWorksAdaptor.findAllOnboardingWorksList();
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, result);
    }

}
