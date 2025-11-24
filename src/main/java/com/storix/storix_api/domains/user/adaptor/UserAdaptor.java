package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.User;
import com.storix.storix_api.domains.user.dto.LoginInfo;
import com.storix.storix_api.domains.user.repository.UserRepository;
import com.storix.storix_api.domains.user.dto.CreateArtistUserCommand;
import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import com.storix.storix_api.global.apiPayload.exception.user.ArtistLoginException;
import com.storix.storix_api.global.apiPayload.exception.user.UnknownUserException;
import com.storix.storix_api.global.apiPayload.exception.ErrorResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;

    // TODO: 인덱싱

    // 작가
    // loginId -> userId (회원가입 api 응답, 작품-작가 매칭 용)
    public Long findArtistUserIdByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return artistUser.get().getId();
        }
        throw UnknownUserException.EXCEPTION;
    }

    // loginId -> Optional<User> (로그인 시, loginId가 DB에 존재하는가? 존재한다면 password까지)
    public LoginInfo findArtistUserLoginInfoByLoginId(String loginId){

        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);

        if(!artistUser.isPresent()) { throw ArtistLoginException.EXCEPTION; }

        LoginInfo loginInfo = new LoginInfo(artistUser.get().getLoginId(), artistUser.get().getPassword());

        return loginInfo;

    }

    public AuthUserDetails findArtistUserIdAndRoleByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return new AuthUserDetails(String.valueOf(artistUser.get().getId()), String.valueOf(artistUser.get().getRole()));
        }
        throw UnknownUserException.EXCEPTION;
    }

    public ErrorResponse isLoginIdDuplicate(String loginId) {
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if (artistUser.isPresent()) {
            return new ErrorResponse(ErrorCode.BAD_REQUEST);
        }
        return null;
    }

    public User saveArtistUser(CreateArtistUserCommand cmd) {
        User user = userRepository.save(cmd.toEntity());
        return userRepository.save(user);
    }
}
