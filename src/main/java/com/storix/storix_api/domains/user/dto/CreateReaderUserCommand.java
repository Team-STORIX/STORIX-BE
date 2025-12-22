package com.storix.storix_api.domains.user.dto;

import com.storix.storix_api.domains.user.domain.*;

public record CreateReaderUserCommand(
        OAuthProvider provider,
        String oid,
        String nickName,
        Gender gender,
        FavoriteGenre favoriteGenre
) {
    public User toEntity() {
        OAuthInfo oauthInfo = new OAuthInfo(provider, oid);
        Profile profile = new Profile(gender, favoriteGenre);

        return User.builder()
                .oauthInfo(oauthInfo)
                .nickName(nickName)
                .profile(profile)
                .build();
    }
}