package com.storix.infrastructure.global.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
public record AccessTokenInfo (
    Long userId,
    String role
) {
}

