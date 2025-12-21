package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.OnboardingToken;
import com.storix.storix_api.domains.user.domain.RefreshToken;
import com.storix.storix_api.domains.user.repository.OnboardingTokenRepository;
import com.storix.storix_api.domains.user.repository.RefreshTokenRepository;
import com.storix.storix_api.global.apiPayload.exception.user.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenAdaptor {

    private final RefreshTokenRepository refreshTokenRepository;
    private final OnboardingTokenRepository onboardingTokenRepository;

    // RefreshToken
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);
    }

    public Long findUserIdByRefreshToken(String refreshToken) {
        Optional<RefreshToken> refreshTokenInfo = refreshTokenRepository.findByRefreshToken(refreshToken);
        if (refreshTokenInfo.isEmpty()) {
            throw InvalidTokenException.EXCEPTION;
        }

        return refreshTokenInfo.get().getId();
    }

    public void deleteRefreshTokenByUserId(Long userId) {
        refreshTokenRepository.deleteById(userId);
    }

    // OnboardingToken
    public void saveOnboardingToken(OnboardingToken onboardingToken) {
        onboardingTokenRepository.save(onboardingToken);
    }

    public void deleteOnboardingTokenByJti(String jti) { onboardingTokenRepository.deleteById(jti);}

}
