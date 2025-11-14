package com.storix.storix_api.controller.dto;

public record ArtistLoginRequest(
        String loginId,
        String password
) {
}
