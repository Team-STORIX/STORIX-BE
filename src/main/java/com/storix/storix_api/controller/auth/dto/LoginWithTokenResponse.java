package com.storix.storix_api.controller.auth.dto;

import lombok.Builder;

@Builder
public record LoginWithTokenResponse(
        String accessToken,
        String refreshToken
) {
}
