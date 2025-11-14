package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.RefreshToken;
import com.storix.storix_api.domains.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenAdaptor {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token")); // 커스텀 에러로 변경
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId.toString());
    }
}
