package com.storix.storix_api.controller.auth.dto;

public record ArtistSignupResponse(
        Long userId,
        String loginId,
        String nickname
) {
}
