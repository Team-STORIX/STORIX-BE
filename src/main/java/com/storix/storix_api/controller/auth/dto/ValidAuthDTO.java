package com.storix.storix_api.controller.auth.dto;

public record ValidAuthDTO(
        boolean isRegistered,
        String idToken
) {
}
