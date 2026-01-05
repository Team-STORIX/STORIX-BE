package com.storix.storix_api.domains.profile.application.service;

import com.storix.storix_api.domains.profile.dto.UserInfo;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserAdaptor userAdaptor;

    // 독자 프로필 조회
    public UserInfo getReaderProfileInfo(Long userId) {
        User readerUser = userAdaptor.findUserById(userId);

        return UserInfo.builder()
                .role(readerUser.getRole().getValue())
                .nickName(readerUser.getNickName())
                .level(readerUser.getLevel())
                .profileDescription(readerUser.getProfileDescription())
                .profileImageUrl(readerUser.getProfileImageUrl())
                .build();
    }

    // 작가 프로필 조회
    public UserInfo getArtistProfileInfo(Long userId) {
        User artistUser = userAdaptor.findUserById(userId);

        return UserInfo.builder()
                .role(artistUser.getRole().getValue())
                .nickName(artistUser.getNickName())
                .profileDescription(artistUser.getProfileDescription())
                .profileImageUrl(artistUser.getProfileImageUrl())
                .build();
    }

}
