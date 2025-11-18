package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.RefreshToken;
import com.storix.storix_api.domains.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenAdaptor {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    // logout 기능 구현용
//    public RefreshToken findByRefreshToken(String refreshToken) {
//        return refreshTokenRepository.findByRefreshToken(refreshToken)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token")); // 커스텀 에러로 변경
//    }
//
//    public void deleteByUserId(Long userId) {
//        refreshTokenRepository.deleteById(userId.toString());
//    }
}
