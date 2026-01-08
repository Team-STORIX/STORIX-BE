package com.storix.storix_api.domains.profile.application.service;

import com.storix.storix_api.domains.profile.dto.UserInfo;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 독자 닉네임 중복 체크
    @Transactional(readOnly = true)
    public void validNickname(String nickName, Long userId) {
        userAdaptor.checkNicknameDuplicateWithArtists(nickName);
        userAdaptor.checkNicknameDuplicateExceptSelf(nickName, userId);
    }

    // 독자 닉네임 변경
    @Transactional
    public String changeNickname(String nickName, Long userId) {
        User readerUser = userAdaptor.findUserById(userId);
        readerUser.changeNickName(nickName);
        return nickName;
    }

    // 독자 한 줄 소개 변경
    @Transactional
    public String changeDescription(String profileDescription, Long userId) {
        User readerUser = userAdaptor.findUserById(userId);
        readerUser.changeProfileDescription(profileDescription);
        return profileDescription;
    }
}
