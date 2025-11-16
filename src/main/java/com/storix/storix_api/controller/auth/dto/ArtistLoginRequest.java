package com.storix.storix_api.controller.auth.dto;

public record ArtistLoginRequest(
        String loginId,
        String password
) {
}
