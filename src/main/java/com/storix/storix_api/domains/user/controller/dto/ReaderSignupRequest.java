package com.storix.storix_api.domains.user.controller.dto;

import com.storix.storix_api.domains.user.domain.Gender;
import com.storix.storix_api.domains.works.domain.Genre;

import java.util.Set;

public record ReaderSignupRequest(
    String nickName,
    Gender gender,
    Set<Genre> favoriteGenreList,
    Set<Long> favoriteWorksIdList
) {
}
