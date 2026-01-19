package com.storix.storix_api.domains.feed.usecase;

import com.storix.storix_api.UseCase;
import com.storix.storix_api.domains.feed.dto.BoardWrapperDto;
import com.storix.storix_api.domains.feed.dto.ReaderBoardReplyInfoWithProfile;
import com.storix.storix_api.domains.feed.service.FeedService;
import com.storix.storix_api.domains.profile.dto.ReaderBoardWithProfileInfo;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@UseCase
@RequiredArgsConstructor
public class FeedUseCase {

    private final FeedService feedService;

    public CustomResponse<Slice<ReaderBoardWithProfileInfo>> getAllReaderBoard(Long userId, Pageable pageable) {

        Slice<ReaderBoardWithProfileInfo> result = feedService.getAllReaderBoard(userId, pageable);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, result);
    }

    public CustomResponse<Slice<ReaderBoardWithProfileInfo>> getReaderBoard(Long userId, Long worksId, Pageable pageable) {

        Slice<ReaderBoardWithProfileInfo> result = feedService.findAllReaderBoardFeedByWorksId(userId, worksId, pageable);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, result);
    }

    public CustomResponse<BoardWrapperDto<ReaderBoardReplyInfoWithProfile>> getReaderBoardDetail(Long userId, Long boardId, Pageable pageable) {

        BoardWrapperDto<ReaderBoardReplyInfoWithProfile> result = feedService.findReaderBoardDetail(userId, boardId, pageable);
        return CustomResponse.onSuccess(SuccessCode.SUCCESS, result);
    }

}
