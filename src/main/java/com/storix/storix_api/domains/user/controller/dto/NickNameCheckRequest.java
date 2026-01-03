package com.storix.storix_api.domains.user.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record NickNameCheckRequest(
        @NotBlank(message = "닉네임은 필수입니다.")
        @Size(max = 10, message = "닉네임은 최대 10자까지 가능합니다.")
        @Pattern(
                regexp = "^(?=.*[가-힣a-zA-Z0-9])[가-힣a-zA-Z0-9 ]+$",
                message = "닉네임은 한글, 영문, 숫자, 공백만 사용할 수 있으며 공백만으로는 불가능합니다."
        )
        String nickName
) {
}
