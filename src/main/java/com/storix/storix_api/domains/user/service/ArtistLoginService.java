package com.storix.storix_api.domains.user.service;

import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArtistLoginService implements UserDetailsService {

    private final UserAdaptor userAdaptor;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userAdaptor.findArtistUserIdAndRoleByLoginId(username);
    }

    public boolean artistLogin(String loginId, String password) {
        User artistUser = userAdaptor.findArtistUserByLoginId(loginId);

        if (!passwordEncoder.matches(password, artistUser.getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        return true;
    }
}
