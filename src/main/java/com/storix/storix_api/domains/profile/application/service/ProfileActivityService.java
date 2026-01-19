package com.storix.storix_api.domains.profile.application.service;

import com.storix.storix_api.domains.plus.application.helper.ReaderBoardHelper;
import com.storix.storix_api.domains.plus.dto.ReaderBoardInfo;
import com.storix.storix_api.domains.profile.dto.ReaderBoardWithProfileInfo;
import com.storix.storix_api.domains.user.adaptor.UserAdaptor;
import com.storix.storix_api.domains.user.dto.StandardProfileInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileActivityService {

    private final UserAdaptor userAdaptor;

    private final ReaderBoardHelper readerBoardHelper;

    // 댓글 조회
    // 좋아요 조회

    // 내가 쓴 게시글 리스트 조회
    @Transactional(readOnly = true)
    public Slice<ReaderBoardWithProfileInfo> findAllReaderBoardList(Long userId, Pageable pageable) {

        // 1) 내 프로필 정보
        StandardProfileInfo profileInfo =
                userAdaptor.findStandardProfileInfoByUserId(userId);

        // 2) 내 게시글 정보
        Slice<ReaderBoardInfo> boards =
                readerBoardHelper.findReaderBoardInfo(userId, null, pageable);

        return readerBoardHelper.map(boards, boardInfo -> profileInfo);
    }

}
