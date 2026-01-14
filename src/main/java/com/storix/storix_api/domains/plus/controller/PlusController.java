package com.storix.storix_api.domains.plus.controller;

import com.storix.storix_api.domains.plus.application.usecase.BoardUseCase;
import com.storix.storix_api.domains.plus.application.usecase.ReviewUseCase;
import com.storix.storix_api.domains.plus.controller.dto.ArtistBoardUploadRequest;
import com.storix.storix_api.domains.plus.controller.dto.ReaderBoardUploadRequest;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewRedirectResponse;
import com.storix.storix_api.domains.plus.controller.dto.ReaderReviewUploadRequest;
import com.storix.storix_api.domains.user.adaptor.AuthUserDetails;
import com.storix.storix_api.global.apiPayload.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/plus")
@RequiredArgsConstructor
@Tag(name = "[+] 탭", description = "[+] 탭 관련 API")
public class PlusController {

    private final BoardUseCase boardUseCase;
    private final ReviewUseCase reviewUseCase;

    @Operation(summary = "독자 게시물 등록", description = "독자 게시물을 등록하는 api 입니다.   \n이미지를 선택한 직후의 렌더링은 프론트에서 진행해주시고, 이미지를 S3 버킷에 업로드한 후 objectKey와 함께 호출해주세요.")
    @PostMapping("/reader-board")
    public ResponseEntity<CustomResponse<Void>> uploadReaderBoard(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @RequestBody ReaderBoardUploadRequest req
    ) {
        return ResponseEntity.ok()
                .body(boardUseCase.createReaderBoard(authUserDetails.getUserId(), req));
    }

    @Operation(summary = "독자 리뷰 등록", description = "독자 리뷰를 등록하는 api 입니다.")
    @PostMapping("/reader-review")
    public ResponseEntity<CustomResponse<ReaderReviewRedirectResponse>> uploadReaderReview(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @RequestBody ReaderReviewUploadRequest req
    ) {
        return ResponseEntity.ok()
                .body(reviewUseCase.createReaderReview(authUserDetails.getUserId(), req));
    }

    @Operation(summary = "작가 게시물 등록", description = "작가 게시물을 등록하는 api 입니다.   \n이미지를 선택한 직후의 렌더링은 프론트에서 진행해주시고, 이미지를 S3 버킷에 업로드한 후 objectKey와 함께 호출해주세요.")
    @PostMapping("/artist-board")
    public ResponseEntity<CustomResponse<Void>> uploadArtistBoard(
            @AuthenticationPrincipal AuthUserDetails authUserDetails,
            @RequestBody ArtistBoardUploadRequest req
    ) {
        return ResponseEntity.ok()
                .body(boardUseCase.createArtistBoard(authUserDetails.getUserId(), req));
    }

}