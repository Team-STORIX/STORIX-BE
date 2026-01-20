package com.storix.storix_api.domains.profile.application.service;

import com.storix.storix_api.domains.feed.domain.ReaderBoardReply;
import com.storix.storix_api.domains.feed.dto.ReaderBoardReplyInfoWithProfile;
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

import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    // 내가 쓴 댓글 리스트 조회
    @Transactional(readOnly = true)
    public Slice<ReaderBoardReplyInfoWithProfile> findAllReaderBoardReplyList(Long userId, Pageable pageable) {

        // 1) 내 프로필 정보
        StandardProfileInfo profileInfo =
                userAdaptor.findStandardProfileInfoByUserId(userId);

        // 2) 내 댓글 정보
        Slice<ReaderBoardReply> replies =
                readerBoardHelper.findReaderBoardReplyInfo(userId, pageable);

        return readerBoardHelper.mapMyRepliesWithProfileAndLike(userId, profileInfo, replies);
    }

    // 내가 누른 좋아요 게시글 리스트 조회
    @Transactional(readOnly = true)
    public Slice<ReaderBoardWithProfileInfo> findAllReaderBoardsLikeList(Long userId, Pageable pageable) {

        // 1) 좋아요 누른 게시글 정보
        Slice<ReaderBoardInfo> boards =
                readerBoardHelper.findLikedReaderBoardInfo(userId, pageable);

        // 2) 유저 id 리스트
        List<Long> userIds = boards.getContent().stream()
                .map(ReaderBoardInfo::userId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 3) 프로필 조회
        Map<Long, StandardProfileInfo> profileMap =
                userAdaptor.findStandardProfileInfoByUserIds(userIds);

        // 4) 최종 매핑
        return readerBoardHelper.map(
                boards,
                boardInfo -> profileMap.get(boardInfo.userId())
        );
    }
}
