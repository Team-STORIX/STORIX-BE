package com.storix.storix_api.domains.favorite.adaptor;

import com.storix.storix_api.domains.favorite.domain.FavoriteWorks;
import com.storix.storix_api.domains.favorite.repository.FavoriteWorksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavoriteWorksAdaptor {

    private final FavoriteWorksRepository favoriteWorksRepository;

    // 온보딩 관심 작품 리스트 저장
    public void saveFavoriteWorks(Long userId, Set<Long> worksIds) {
        List<FavoriteWorks> entities = worksIds.stream()
                .map(worksId -> new FavoriteWorks(userId, worksId))
                .toList();

        favoriteWorksRepository.saveAll(entities);
    }

    // 회원 탈퇴 시 관심 작품 리스트 제거
    public void deleteFavoriteWorks(Long userId) {
        favoriteWorksRepository.deleteByUserId(userId);
    }

}
