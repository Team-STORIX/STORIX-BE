package com.storix.storix_api.domains.preference.service;

import ch.qos.logback.core.status.ErrorStatus;
import com.nimbusds.oauth2.sdk.GeneralException;
import com.storix.storix_api.domains.library.dto.LibraryWorksInfo;
import com.storix.storix_api.domains.preference.application.helper.ExplorationCacheHelper;
import com.storix.storix_api.domains.preference.application.usecase.ExplorationUseCase;
import com.storix.storix_api.domains.preference.dto.ExplorationResultResponseDto;
import com.storix.storix_api.domains.preference.dto.ExplorationSubmitRequestDto;
import com.storix.storix_api.domains.preference.dto.ExplorationWorksResponseDto;
import com.storix.storix_api.domains.preference.dto.GenreScoreInfo;
import com.storix.storix_api.domains.preference.repository.ExplorationRepository;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.global.apiPayload.exception.preference.DuplicatedExplorationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExplorationService implements ExplorationUseCase {

    private final ExplorationRepository explorationRepository;
    private final LoadWorksPort loadWorksPort;
    private final ExplorationCacheHelper cacheHelper;

    @Override
    @Transactional(readOnly = true)
    public List<ExplorationWorksResponseDto> getExplorationWorks(Long userId) {
        // [제약조건] 오늘 이미 15개를 다 채워서 결과를 본 유저인지 확인
        if (cacheHelper.isAlreadyParticipatedToday(userId)) {
            return Collections.emptyList();
        }

        // 이미 응답한 작품 ID 가져오기 (PWA 상태 유지용)
        List<Long> respondedIds = explorationRepository.findRespondedWorksIdsByUserId(userId);
        int needed = 15 - respondedIds.size();

        if (needed <= 0) return Collections.emptyList();

        // 인덱스를 타는 ID 기반 랜덤 조회 (성능 최적화)
        return loadWorksPort.findRandomWorksExcluding(respondedIds, needed)
                .stream()
                .map(ExplorationWorksResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void submitExploration(Long userId, ExplorationSubmitRequestDto request) {
        // [제약조건] 오늘 참여 완료 여부 재검증
        if (cacheHelper.isAlreadyParticipatedToday(userId)) {
            throw DuplicatedExplorationException.EXCEPTION;
        }

        // 1. 개별 응답 저장
        explorationRepository.save(request.toEntity(userId));

        // 2. 현재까지 응답한 개수 확인
        List<Long> respondedIds = explorationRepository.findRespondedWorksIdsByUserId(userId);

        // 3. 15개가 완료되었다면 '오늘 참여 완료'로 Redis에 기록 (TTL 자정까지)
        if (respondedIds.size() >= 15) {
            cacheHelper.markAsParticipatedToday(userId);
            cacheHelper.deleteChartCache(userId); // 기존 차트 캐시 무효화하여 갱신 유도
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ExplorationResultResponseDto getExplorationResults(Long userId) {

        // 1. 엔티티 리스트 조회
        List<Works> likedEntities = explorationRepository.findWorksByLikedStatus(userId, true);
        List<Works> dislikedEntities = explorationRepository.findWorksByLikedStatus(userId, false);

        // 2. 변환 후 DTO 생성
        return ExplorationResultResponseDto.builder()
                .likedWorks(toLibraryWorksInfoList(likedEntities))
                .dislikedWorks(toLibraryWorksInfoList(dislikedEntities))
                .genreScores(getGenreChart(userId))
                .build();
    }

    // 🔄 변환 헬퍼 메서드 (Record 생성자 사용)
    private List<LibraryWorksInfo> toLibraryWorksInfoList(List<Works> worksList) {
        return worksList.stream()
                .map(w -> new LibraryWorksInfo(
                        // 1. 작품 정보 (엔티티에서 꺼내옴)
                        w.getId(),
                        w.getWorksName(),
                        w.getArtistName(),
                        w.getThumbnailUrl(),
                        w.getWorksType().getDbValue(), // Enum -> String 변환
                        w.getGenre().getDbValue(),     // Enum -> String 변환

                        // 2. 리뷰 정보 (취향 탐색 결과에는 없으므로 null 처리)
                        null, // reviewId
                        null  // rating
                ))
                .toList();
    }

    private List<GenreScoreInfo> getGenreChart(Long userId) {
        return cacheHelper.getOrGenerateChart(userId, () -> {
            List<Object[]> rawCounts = explorationRepository.countLikedGenresByUserId(userId);
            long totalLiked = rawCounts.stream().mapToLong(row -> (long) row[1]).sum();

            if (totalLiked == 0) return Collections.emptyList();

            return rawCounts.stream()
                    .map(row -> new GenreScoreInfo(
                            row[0].toString(),
                            Math.round(((long) row[1] / (double) totalLiked) * 5.0 * 10) / 10.0
                    ))
                    .toList();
        });
    }
}