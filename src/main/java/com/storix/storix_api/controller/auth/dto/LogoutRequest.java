package com.storix.storix_api.controller.auth.dto;

public record LogoutRequest(
        String refreshToken
) {
}
