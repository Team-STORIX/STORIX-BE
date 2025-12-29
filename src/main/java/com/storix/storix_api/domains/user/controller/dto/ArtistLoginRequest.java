package com.storix.storix_api.domains.user.controller.dto;

public record ArtistLoginRequest(
        String loginId,
        String password
) {
}
