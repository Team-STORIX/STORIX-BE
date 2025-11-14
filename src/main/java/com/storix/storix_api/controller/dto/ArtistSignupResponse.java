package com.storix.storix_api.controller.dto;

public record ArtistSignupResponse(
        Long userId,
        String loginId,
        String nickname
) {
}
