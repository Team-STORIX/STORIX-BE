package com.storix.storix_api.domains.onboarding.adaptor;

import com.storix.storix_api.domains.onboarding.repository.OnboardingWorksRepository;
import com.storix.storix_api.domains.onboarding.domain.OnboardingWorks;
import com.storix.storix_api.global.apiPayload.exception.user.InvalidOnboardingWorksException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OnboardingWorksAdaptor {

    private final OnboardingWorksRepository onboardingWorksRepository;

    public List<OnboardingWorks> findAllOnboardingWorksList() {
        return onboardingWorksRepository.findAll();
    }

    public void checkReaderSignUpWithOnboardingWorksList(Set<Long> worksIds) {

        if (worksIds == null || worksIds.isEmpty()) {
            throw InvalidOnboardingWorksException.EXCEPTION;
        }

        long existCount =
                onboardingWorksRepository.countByWorksIds(worksIds);

        if (existCount != worksIds.size()) {
            throw InvalidOnboardingWorksException.EXCEPTION;
        }

    }

}
