package com.storix.storix_api.domains.topicroom.controller;

import com.storix.storix_api.domains.search.dto.SearchResponseWrapperDto;
import com.storix.storix_api.domains.topicroom.application.usecase.TopicRoomUseCase;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomCreateRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomReportRequestDto;
import com.storix.storix_api.domains.topicroom.dto.TopicRoomResponseDto;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import com.storix.storix_api.global.apiPayload.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topic-rooms")
@RequiredArgsConstructor
@Tag(name = "토픽룸", description = "토픽룸 REST API")
public class TopicRoomController {

    private final TopicRoomUseCase topicRoomUseCase;

    // 1. 참여 목록
    @GetMapping("/me")
    @Operation(summary = "참여 중인 토픽룸 목록", description = "내가 참여 중인 토픽룸 리스트를 반환합니다.")
    public CustomResponse<Slice<TopicRoomResponseDto>> getMyRooms(
            @AuthenticationPrincipal Long userId,
            @PageableDefault(size = 3) Pageable pageable) {

        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                topicRoomUseCase.getMyJoinedRooms(userId, pageable));
    }

    // 2. 오늘의 토픽룸
    @GetMapping("/today")
    @Operation(summary = "오늘의 토픽룸 목록", description = "오늘의 토픽룸 리스트를 반환합니다. 활성 사용자가 많은 토픽룸 3개가 포함됩니다.")
    public CustomResponse<List<TopicRoomResponseDto>> getTodayTop3(@AuthenticationPrincipal Long userId) {

        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                topicRoomUseCase.getTodayTrendingRooms(userId));
    }

    // 3. 검색
    @GetMapping("/search")
    @Operation(summary = "토픽룸 검색", description = "토픽룸 검색 리스트를 반환합니다.")
    public CustomResponse<SearchResponseWrapperDto<TopicRoomResponseDto>> search(
            @RequestParam String keyword, Pageable pageable) {

        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                topicRoomUseCase.searchRooms(keyword, pageable));
    }

    // 4. 생성 (New)
    @PostMapping
    @Operation(summary = "토픽룸 생성", description = "토픽룸을 생성합니다. 작품 선택 및 제목 설정은 필수입니다.")
    public CustomResponse<Long> create(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody TopicRoomCreateRequestDto request) {

        return CustomResponse.onSuccess(
                SuccessCode.SUCCESS,
                topicRoomUseCase.createRoom(userId, request));
    }

    // 5. 입장
    @PostMapping("/{roomId}/join")
    @Operation(summary = "토픽룸 입장", description = "토픽룸에 참여합니다. 한 사용자는 최대 9개까지 참여할 수 있습니다.")
    public CustomResponse<String> join(@AuthenticationPrincipal Long userId, @PathVariable Long roomId) {

        topicRoomUseCase.joinRoom(userId, roomId);

        return CustomResponse.onSuccess(SuccessCode.SUCCESS);
    }

    // 6. 퇴장
    @DeleteMapping("/{roomId}/leave")
    @Operation(summary = "토픽룸 퇴장", description = "참여 중이던 토픽룸에서 퇴장합니다.")
    public CustomResponse<String> leave(@AuthenticationPrincipal Long userId, @PathVariable Long roomId) {

        topicRoomUseCase.leaveRoom(userId, roomId);

        return CustomResponse.onSuccess(SuccessCode.SUCCESS);
    }

    // 7. 신고
    @PostMapping("/{roomId}/report")
    @Operation(summary = "토픽룸 사용자 신고", description = "토픽룸 사용자를 신고합니다. 사유는 3개 중 선택 가능하며, 기타 사유의 경우 최대 100자 제한입니다.")
    public CustomResponse<String> report(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long roomId,
            @Valid @RequestBody TopicRoomReportRequestDto request) {

        topicRoomUseCase.reportUser(userId, roomId, request);

        return CustomResponse.onSuccess(SuccessCode.SUCCESS);
    }
}