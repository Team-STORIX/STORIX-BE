package com.storix.storix_api.controller.auth.dto;

import com.storix.storix_api.domains.user.domain.FavoriteGenre;
import com.storix.storix_api.domains.user.domain.Gender;
import com.storix.storix_api.domains.user.domain.OAuthProvider;

public record ReaderSignupRequest(
    OAuthProvider oauthProvider,
    String oid,
    String nickName,
    Gender gender,
    FavoriteGenre favoriteGenre
) {
}
