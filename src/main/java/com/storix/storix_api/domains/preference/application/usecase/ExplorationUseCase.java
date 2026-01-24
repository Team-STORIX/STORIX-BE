package com.storix.storix_api.domains.preference.application.usecase;

import com.storix.storix_api.domains.preference.dto.*;
import java.util.List;

public interface ExplorationUseCase {

    // 1) 탐색할 작품 목록 조회 (1일 1회 제한 및 중복 제외)
    List<ExplorationWorksResponseDto> getExplorationWorks(Long userId);

    // 2) 개별 작품 응답 제출 (PWA 상태 유지 및 15개 완료 시 마킹)
    void submitExploration(Long userId, ExplorationSubmitRequestDto request);

    // 3) 결과 모아보기 (좋아요/별로예요 리스트 + 장르 차트)
    ExplorationResultResponseDto getExplorationResults(Long userId);
}