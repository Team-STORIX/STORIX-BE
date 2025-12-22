package com.storix.storix_api.domains.user.domain;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Profile {

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private FavoriteGenre favoriteGenre;

    @Builder
    public Profile(Gender gender, FavoriteGenre favoriteGenre) {
        this.gender = gender;
        this.favoriteGenre = favoriteGenre;
    }

}
