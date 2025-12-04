package com.storix.storix_api.controller.auth.dto;

public record ReaderLoginResponse(
        boolean isRegistered,
        String accessToken,
        String refreshToken
) {
    public static ReaderLoginResponse of(String accessToken, String refreshToken) {
        return new ReaderLoginResponse(true, accessToken, refreshToken);
    }
}
