package com.storix.storix_api.controller.auth.dto;

public record ArtistSignupRequest(
        String nickName,
        String loginId,
        String password
) {
}
