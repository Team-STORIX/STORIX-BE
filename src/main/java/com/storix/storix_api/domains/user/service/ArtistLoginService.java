package com.storix.storix_api.domains.user.service;

import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.User;
import com.storix.storix_api.global.apiPayload.exception.ArtistLoginException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistLoginService implements UserDetailsService {

    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAdaptor.findArtistUserIdAndRoleByLoginId(username);
    }

    public boolean isArtistLoginValidate(String loginId, String password) {
        Optional<User> artistUser = userAdaptor.findArtistUserByLoginId(loginId);

        if (!artistUser.isPresent() | !passwordEncoder.matches(password, artistUser.get().getPassword())) {
            throw ArtistLoginException.EXCEPTION;
        }

        return true;
    }
}
