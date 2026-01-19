package com.storix.storix_api.domains.feed.service;

import com.storix.storix_api.domains.feed.adaptor.ReaderFeedAdaptor;
import com.storix.storix_api.domains.feed.domain.ReaderBoardReply;
import com.storix.storix_api.domains.feed.dto.BoardWrapperDto;
import com.storix.storix_api.domains.feed.dto.ReaderBoardReplyInfo;
import com.storix.storix_api.domains.feed.dto.ReaderBoardReplyInfoWithProfile;
import com.storix.storix_api.domains.plus.application.helper.ReaderBoardHelper;
import com.storix.storix_api.domains.plus.domain.ReaderBoard;
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
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserAdaptor userAdaptor;
    private final ReaderFeedAdaptor readerFeedAdaptor;
    private final ReaderBoardHelper readerBoardHelper;

    @Transactional(readOnly = true)
    public Slice<ReaderBoardWithProfileInfo> getAllReaderBoard(Long userId, Pageable pageable) {

        // 1) 최신순 게시글
        Slice<ReaderBoard> boards = readerFeedAdaptor.findAllByOrderByCreatedAtDesc(pageable);

        List<Long> boardIds = boards.getContent().stream()
                .map(ReaderBoard::getId)
                .toList();

        // 2) 좋아요 여부
        Set<Long> likedBoardIds = readerFeedAdaptor.findLikedBoardIds(userId, boardIds);


        Slice<ReaderBoardInfo> boardInfos = boards.map(board ->
                ReaderBoardInfo.ofFeedBoard(board, likedBoardIds.contains(board.getId()))
        );

        // 3) 프로필 매핑
        List<Long> writerIds = boardInfos.getContent().stream()
                .map(ReaderBoardInfo::userId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, StandardProfileInfo> profileMap =
                userAdaptor.findStandardProfileInfoByUserIds(writerIds);

        // 최종 매핑
        return readerBoardHelper.map(boardInfos, info ->
                profileMap.get(info.userId()));
    }

    @Transactional(readOnly = true)
    public Slice<ReaderBoardWithProfileInfo> findAllReaderBoardFeedByWorksId(Long userId, Long worksId, Pageable pageable) {

        // 1) 게시글 정보
        Slice<ReaderBoardInfo> boards =
                readerBoardHelper.findReaderBoardInfo(userId, worksId, pageable);

        // 유저 id 리스트
        List<Long> userIds = boards.getContent().stream()
                .map(ReaderBoardInfo::userId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 2) 프로필 정보
        Map<Long, StandardProfileInfo> profileMap =
                userAdaptor.findStandardProfileInfoByUserIds(userIds);

        return readerBoardHelper.map(boards, boardInfo ->
                profileMap.get(boardInfo.userId())
        );
    }

    @Transactional(readOnly = true)
    public BoardWrapperDto<ReaderBoardReplyInfoWithProfile> findReaderBoardDetail(Long userId, Long boardId, Pageable replyPageable) {

        // 1) 게시글 단건 정보
        ReaderBoardInfo boardInfo = readerBoardHelper.findSingleReaderBoardInfo(userId, boardId, true);

        StandardProfileInfo writerProfile =
                userAdaptor.findStandardProfileInfoByUserId(boardInfo.userId());

        ReaderBoardWithProfileInfo board =
                readerBoardHelper.mapSingle(boardInfo, writerProfile);

        // 2) 댓글 정보
        Slice<ReaderBoardReply> replySlice =
                readerFeedAdaptor.findAllByBoardId(boardId, replyPageable);

        Slice<ReaderBoardReplyInfoWithProfile> comments =
                readerBoardHelper.mapRepliesWithProfileAndLike(userId, replySlice);

        return new BoardWrapperDto<>(board, comments);
    }

}
