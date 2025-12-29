package com.storix.storix_api.domains.user.controller.dto;

import com.storix.storix_api.domains.user.domain.FavoriteGenre;
import com.storix.storix_api.domains.user.domain.Gender;
import com.storix.storix_api.domains.user.domain.OAuthProvider;

public record ReaderSignupRequest(
    String nickName,
    Gender gender,
    FavoriteGenre favoriteGenre
) {
}
