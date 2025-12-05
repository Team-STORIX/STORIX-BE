package com.storix.storix_api.domains.user.application.service;

import com.storix.storix_api.domains.user.adaptor.RefreshTokenAdaptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService {

    private final RefreshTokenAdaptor refreshTokenAdaptor;

    @Transactional
    public void logoutByRefreshToken(String refreshToken) {
        Long userId = refreshTokenAdaptor.findUserIdByRefreshToken(refreshToken);
        refreshTokenAdaptor.deleteByUserId(userId);
    }

}
