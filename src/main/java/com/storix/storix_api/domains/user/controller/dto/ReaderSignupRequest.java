package com.storix.storix_api.domains.user.controller.dto;

import com.storix.storix_api.domains.user.domain.Gender;
import com.storix.storix_api.domains.works.domain.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record ReaderSignupRequest(
    @NotBlank(message = "닉네임은 필수입니다.")
    @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
    @Pattern(
            regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9 ]+$",
            message = "닉네임은 한글, 영문, 숫자, 공백만 사용할 수 있으며 공백만으로는 불가능합니다."
    )
    String nickName,
    Gender gender,
    Set<Genre> favoriteGenreList,
    Set<Long> favoriteWorksIdList
) {
}
