package com.storix.storix_api.controller.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record LoginWithTokenResponse(
        String accessToken,
        String refreshToken
) {
}
