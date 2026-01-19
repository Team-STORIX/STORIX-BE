package com.storix.storix_api.domains.plus.application.helper;

import com.storix.storix_api.domains.hashtag.adaptor.HashtagAdaptor;
import com.storix.storix_api.domains.plus.adaptor.BoardImageAdaptor;
import com.storix.storix_api.domains.plus.dto.ReaderBoardImageInfo;
import com.storix.storix_api.domains.plus.dto.ReaderBoardInfo;
import com.storix.storix_api.domains.profile.dto.ReaderBoardWithProfileInfo;
import com.storix.storix_api.domains.user.dto.StandardProfileInfo;
import com.storix.storix_api.domains.works.application.port.LoadWorksPort;
import com.storix.storix_api.domains.works.dto.WorksInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ReaderBoardHelper {

    private final BoardImageAdaptor boardImageAdaptor;
    private final HashtagAdaptor hashTagAdaptor;

    private final LoadWorksPort loadWorksPort;

    public Slice<ReaderBoardWithProfileInfo> map(
            Slice<ReaderBoardInfo> boards,
            Function<ReaderBoardInfo, StandardProfileInfo> profileResolver
    ) {
        List<ReaderBoardInfo> content = boards.getContent();
        if (content.isEmpty()) return boards.map(b -> null);

        // 게시글 id 리스트
        List<Long> boardIds = content.stream()
                .map(ReaderBoardInfo::boardId)
                .toList();

        // 1) 게시글 이미지 매핑
        Map<Long, List<ReaderBoardImageInfo>> imageMap =
                boardImageAdaptor.findReaderBoardImagesByBoardIds(boardIds);

        // 작품 id 리스트
        List<Long> worksIds = content.stream()
                .map(ReaderBoardInfo::worksId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // 2) 게시글 작품 정보 매핑
        Map<Long, WorksInfo> worksMap = worksIds.isEmpty()
                ? Collections.emptyMap()
                : loadWorksPort.findAllWorksInfoByWorksIds(worksIds);

        // 3) 게시글 작품 해시태그 정보 매핑
        Map<Long, List<String>> hashtagMap = worksIds.isEmpty()
                ? Collections.emptyMap()
                : hashTagAdaptor.findHashTagsByWorksIds(worksIds);

        // 최종 매핑
        return boards.map(boardInfo -> {
            StandardProfileInfo profile = profileResolver.apply(boardInfo);

            Long worksId = boardInfo.worksId();
            return ReaderBoardWithProfileInfo.of(
                    profile,
                    boardInfo,
                    imageMap.getOrDefault(boardInfo.boardId(), List.of()),
                    worksId == null ? null : worksMap.get(worksId),
                    worksId == null ? List.of() : hashtagMap.getOrDefault(worksId, List.of())
            );
        });
    }

}
