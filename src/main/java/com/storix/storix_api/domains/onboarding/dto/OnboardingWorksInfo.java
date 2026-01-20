package com.storix.storix_api.domains.onboarding.dto;

public record OnboardingWorksInfo(
        Long worksId,
        String title,
        String thumbnailUrl,
        String artistName
) {
}
