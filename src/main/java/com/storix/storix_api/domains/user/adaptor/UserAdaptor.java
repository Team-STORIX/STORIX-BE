package com.storix.storix_api.domains.user.adaptor;

import com.storix.storix_api.domains.user.domain.OAuthInfo;
import com.storix.storix_api.domains.user.domain.OAuthProvider;
import com.storix.storix_api.domains.user.domain.Role;
import com.storix.storix_api.domains.user.domain.User;
import com.storix.storix_api.domains.user.dto.CreateReaderUserCommand;
import com.storix.storix_api.domains.user.dto.LoginInfo;
import com.storix.storix_api.domains.user.repository.UserRepository;
import com.storix.storix_api.domains.user.dto.CreateArtistUserCommand;
import com.storix.storix_api.domains.works.repository.WorksRepository;
import com.storix.storix_api.global.apiPayload.exception.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserAdaptor {

    private final UserRepository userRepository;
    private final WorksRepository worksRepository;

    public Role findUserRoleByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            return user.get().getRole();
        }
        throw UnknownUserException.EXCEPTION;
    }

    /**
     * 독자
     * */
    // 독자
    // OAuthInfo(oid, provider) -> userId(PK), role (토큰 서명용)
    public User findReaderUserByOAuthInfo(OAuthInfo oauthInfo) {
        Optional<User> readerUser = userRepository.findByOauthInfoProviderAndOauthInfoOid(oauthInfo.getProvider(), oauthInfo.getOid());
        if (readerUser.isPresent()) {
            return readerUser.get();
        } else {
            throw UnknownUserException.EXCEPTION;
        }
    }

    public boolean isUserPresentWithProviderAndOid(OAuthProvider provider, String oid) {
        Optional<User> readerUser = userRepository.findByOauthInfoProviderAndOauthInfoOid(provider, oid);
        return readerUser.isPresent();
    }

    public void checkNicknameDuplicate(String nickName) {
        if (userRepository.existsByActiveNickName(nickName)) {
            throw DuplicateNicknameException.EXCEPTION;
        }
    }

    public void checkNicknameDuplicateExceptSelf(String nickName, Long userId) {
        if (userRepository.existsNickNameExceptSelf(nickName, userId)) {
            throw ProfileDuplicateNicknameException.EXCEPTION;
        }
    }

    public void checkNicknameDuplicateWithArtists(String nickName) {
        if (worksRepository.existsByAnyAuthorName(nickName)) {
            throw ProfileForbiddenNicknameException.EXCEPTION;
        }
    }

    // 독자 회원 가입
    public AuthUserDetails saveReaderUser(CreateReaderUserCommand cmd) {
        try {
            User user = userRepository.save(cmd.toEntity());
            return new AuthUserDetails(user.getId(), user.getRole().toString());
        } catch (DataIntegrityViolationException e) {
            throw DuplicateUserException.EXCEPTION;
        }
    }

    /**
     * 작가
     * */
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

        if(artistUser.isEmpty()) { throw ArtistLoginException.EXCEPTION; }

        return new LoginInfo(artistUser.get().getLoginId(), artistUser.get().getPassword());
    }

    public User findArtistUserByLoginId(String loginId){
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if(artistUser.isPresent()){
            return artistUser.get();
        }
        throw UnknownUserException.EXCEPTION;
    }

    public void isLoginIdDuplicate(String loginId) {
        Optional<User> artistUser = userRepository.findArtistUserByLoginId(loginId);
        if (artistUser.isPresent()) {
            throw DuplicateUserException.EXCEPTION;
        }
    }

    public void saveArtistUser(CreateArtistUserCommand cmd) { userRepository.save(cmd.toEntity()); }

    public User findUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw UnknownUserException.EXCEPTION;
        }
        return user.get();
    }
}
