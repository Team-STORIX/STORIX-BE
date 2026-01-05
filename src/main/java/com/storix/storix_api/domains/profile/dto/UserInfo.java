package com.storix.storix_api.domains.profile.dto;

import lombok.Builder;

@Builder
public record UserInfo(
    String profileImageUrl,
    String nickName,
    int level,
    String profileDescription
) {
}
