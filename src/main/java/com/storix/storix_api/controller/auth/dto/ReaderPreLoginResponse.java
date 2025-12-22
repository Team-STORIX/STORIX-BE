package com.storix.storix_api.controller.auth.dto;

import com.storix.storix_api.domains.user.domain.OAuthProvider;

public record ReaderPreLoginResponse(
        boolean isRegistered,
        String onboardingToken
) {
    public static ReaderPreLoginResponse of(String onboardingToken) {
        return new ReaderPreLoginResponse(false, onboardingToken);
    }
}
