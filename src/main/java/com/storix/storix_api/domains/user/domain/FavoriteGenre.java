package com.storix.storix_api.domains.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FavoriteGenre {
    ROMANCE("로맨스"),
    THRILLER("스릴러"),
    FANTASY("판타지"),
    DAILY("일상"),
    ROMANCE_FANTASY("로맨스 판타지"),
    COMEDY("코미디"),
    DRAMA("드라마");

    private String value;
}
