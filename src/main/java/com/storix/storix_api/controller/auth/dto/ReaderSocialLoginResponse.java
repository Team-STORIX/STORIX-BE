package com.storix.storix_api.controller.auth.dto;

public record ReaderSocialLoginResponse(
        boolean isRegistered,
        ReaderLoginResponse readerLoginResponse,
        ReaderPreLoginResponse readerPreLoginResponse
) {
}
