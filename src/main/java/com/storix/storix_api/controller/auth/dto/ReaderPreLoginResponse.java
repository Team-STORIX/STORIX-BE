package com.storix.storix_api.controller.auth.dto;

import com.storix.storix_api.domains.user.domain.OAuthProvider;

public record ReaderPreLoginResponse(
        boolean isRegistered,
        OAuthProvider provider,
        String oid
) {
    public static ReaderPreLoginResponse of(OAuthProvider provider, String oid) {
        return new ReaderPreLoginResponse(false, provider, oid);
    }
}
