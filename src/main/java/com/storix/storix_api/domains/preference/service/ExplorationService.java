package com.storix.storix_api.domains.preference.service;

import com.storix.storix_api.domains.preference.application.helper.ExplorationCacheHelper;
import com.storix.storix_api.domains.preference.application.usecase.ExplorationUseCase;
import com.storix.storix_api.domains.preference.dto.*;
import com.storix.storix_api.domains.preference.repository.ExplorationRepository;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.domain.Genre;
import com.storix.storix_api.domains.works.domain.Works;
import com.storix.storix_api.domains.works.dto.LibraryWorksInfo;
import com.storix.storix_api.global.apiPayload.code.ErrorCode;
import com.storix.storix_api.global.apiPayload.exception.STORIXCodeException;
import com.storix.storix_api.global.apiPayload.exception.preference.DuplicatedExplorationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ExplorationService implements ExplorationUseCase {

    private final ExplorationRepository explorationRepository;
    private final LoadWorksPort loadWorksPort;
    private final ExplorationCacheHelper cacheHelper;

    @Override
    @Transactional(readOnly = true)
    public List<ExplorationWorksResponseDto> getExplorationWorks(Long userId) {
        if (cacheHelper.isAlreadyParticipatedToday(userId)) {
            return Collections.emptyList();
        }

        // 중복 방지
        List<Long> dbHistoryIds = explorationRepository.findRespondedWorksIdsByUserId(userId);
        Set<Long> pendingIds = cacheHelper.getPendingWorksIds(userId);

        Set<Long> allHistoryIds = new HashSet<>(dbHistoryIds);
        allHistoryIds.addAll(pendingIds);

        // 오늘 진행도 계산
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        int todayCount = explorationRepository.countByUserIdAndCreatedAtAfter(userId, startOfToday)
                + pendingIds.size();

        int needed = 15 - todayCount;
        if (needed <= 0) return Collections.emptyList();

        return loadWorksPort.findRandomWorksExcluding(new ArrayList<>(allHistoryIds), needed)
                .stream()
                .map(ExplorationWorksResponseDto::from)
                .toList();
    }

    @Override
    @Transactional
    public void submitExploration(Long userId, ExplorationSubmitRequestDto request) {

        // 작품 존재 여부 확인
        loadWorksPort.checkWorksExistById(request.worksId());

        if (cacheHelper.isAlreadyParticipatedToday(userId)) {
            throw DuplicatedExplorationException.EXCEPTION;
        }

        PendingSwipeDto pendingDto = PendingSwipeDto.builder()
                .userId(userId)
                .worksId(request.worksId())
                .isLiked(request.isLiked())
                .build();

        Long result = cacheHelper.submitWithLua(userId, pendingDto);

        if (result == -1 || result == -2) {
            // 이미 참여 & 동시 요청으로 인해 15개를 넘어간 경우
            cacheHelper.markAsParticipatedToday(userId);
            throw DuplicatedExplorationException.EXCEPTION;
        }

        if (result == -3) {
            throw new STORIXCodeException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (result == -4) {

            // 큐가 가득 찬 상태
            throw new STORIXCodeException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        if (result == 15) {
            cacheHelper.markAsParticipatedToday(userId);
            cacheHelper.deleteChartCache(userId);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public ExplorationResultResponseDto getExplorationResults(Long userId) {

        LocalDateTime threshold = LocalDateTime.now().minusHours(3);

        List<Works> dbLiked = explorationRepository.findWorksByLikedStatusToday(userId, true, threshold);
        List<Works> dbDisliked = explorationRepository.findWorksByLikedStatusToday(userId, false, threshold);

        // Redis 대기열 조회 및 병합
        List<PendingSwipeDto> pending = cacheHelper.getAllPendingSwipes(userId);
        Set<Long> dbIds = Stream.concat(dbLiked.stream(), dbDisliked.stream())
                .map(Works::getId).collect(Collectors.toSet());

        List<Long> pLikedIds = pending.stream().filter(p -> p.isLiked() && !dbIds.contains(p.worksId()))
                .map(PendingSwipeDto::worksId).toList();
        List<Long> pDislikedIds = pending.stream().filter(p -> !p.isLiked() && !dbIds.contains(p.worksId()))
                .map(PendingSwipeDto::worksId).toList();

        // Redis 데이터에 대한 작품 상세 정보 로드
        List<Works> allLiked = new ArrayList<>(dbLiked);
        allLiked.addAll(loadWorksPort.findWorksByIds(pLikedIds));

        List<Works> allDisliked = new ArrayList<>(dbDisliked);
        allDisliked.addAll(loadWorksPort.findWorksByIds(pDislikedIds));

        return ExplorationResultResponseDto.builder()
                .likedWorks(toLibraryWorksInfoList(allLiked))
                .dislikedWorks(toLibraryWorksInfoList(allDisliked))
                .genreScores(calculateTodayScores(allLiked))
                .build();
    }


    @Override
    public List<GenreScoreInfo> getCumulativeStats(Long userId) {
        return cacheHelper.getOrGenerateChart(userId, () -> {
            List<Object[]> raw = explorationRepository.countLikedGenresByUserId(userId);
            return transformToScoreInfo(raw);
        });
    }

    private List<GenreScoreInfo> calculateTodayScores(List<Works> likedWorks) {
        if (likedWorks.isEmpty()) return Collections.emptyList();

        Map<String, Long> genreCounts = likedWorks.stream()
                .collect(Collectors.groupingBy(
                        w -> w.getGenre().getDbValue(),
                        Collectors.counting()
                ));

        long total = likedWorks.size();

        return genreCounts.entrySet().stream()
                .map(entry -> new GenreScoreInfo(
                        entry.getKey(),
                        Math.round((entry.getValue() / (double) total) * 5.0 * 10) / 10.0
                ))
                .toList();
    }

    private List<GenreScoreInfo> transformToScoreInfo(List<Object[]> rawCounts) {
        long totalLiked = rawCounts.stream().mapToLong(row -> (long) row[1]).sum();
        if (totalLiked == 0) return Collections.emptyList();

        return rawCounts.stream()
                .map(row -> new GenreScoreInfo(
                        ((Genre) row[0]).getDbValue(),
                        Math.round(((long) row[1] / (double) totalLiked) * 5.0 * 10) / 10.0
                ))
                .toList();
    }

    private List<LibraryWorksInfo> toLibraryWorksInfoList(List<Works> worksList) {
        return worksList.stream()
                .map(w -> new LibraryWorksInfo(
                        w.getId(),
                        w.getWorksName(),
                        w.getArtistName(),
                        w.getThumbnailUrl(),
                        w.getWorksType().getDbValue(),
                        w.getGenre().getDbValue(),
                        null, null
                ))
                .toList();
    }

    // 세션 시간 계산
    private LocalDateTime getSessionThreshold() {
        return LocalDateTime.now().minusHours(3);
    }
}