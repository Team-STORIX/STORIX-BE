package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.RefreshToken;
import com.storix.storix_api.domains.user.repository.RefreshTokenRepository;
import com.storix.storix_api.global.apiPayload.exception.user.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenAdaptor {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public Long findUserIdByRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenInfo = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (!refreshTokenInfo.isPresent()) {
            throw InvalidTokenException.EXCEPTION;
        }

        return refreshTokenInfo.get().getId();
    }

    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
