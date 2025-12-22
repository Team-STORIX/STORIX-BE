package com.storix.storix_api.controller.auth.dto;

import lombok.Builder;

@Builder
public record OAuthLoginWithTokenResponse(
        String onboardingToken
) {
}
