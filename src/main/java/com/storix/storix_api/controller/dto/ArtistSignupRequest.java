package com.storix.storix_api.controller.dto;

public record ArtistSignupRequest(
        String nickName,
        String loginId,
        String password
) {
}
