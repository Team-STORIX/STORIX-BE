package com.storix.storix_api.controller.auth.dto;

public record OAuthAuthorizationRequest(
        String authCode,
        String redirectUri
) {
}