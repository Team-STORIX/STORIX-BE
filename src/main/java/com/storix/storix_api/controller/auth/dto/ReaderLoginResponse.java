package com.storix.storix_api.controller.auth.dto;

public record ReaderLoginResponse(
        String accessToken,
        String refreshToken
) {
}
