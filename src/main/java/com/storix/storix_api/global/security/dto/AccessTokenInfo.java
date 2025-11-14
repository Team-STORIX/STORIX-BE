package com.storix.storix_api.global.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccessTokenInfo {
    private final Long userId;
    private final String role;
}

