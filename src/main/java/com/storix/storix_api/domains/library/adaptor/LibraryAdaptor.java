package com.storix.storix_api.domains.library.adaptor;

import com.storix.storix_api.domains.library.domain.Library;
import com.storix.storix_api.domains.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryAdaptor {

    private final LibraryRepository libraryRepository;

    // 리뷰 개수 업데이트
    public void incrementReviewCount(Long libraryUserId) {
        libraryRepository.incrementReviewCount(libraryUserId);
    }

    public int decrementReviewCount(Long libraryUserId) {
        return libraryRepository.decrementReviewCount(libraryUserId);
    }

    // 게시물 개수 업데이트
    public void incrementBoardCount(Long libraryUserId) {
        libraryRepository.incrementBoardCount(libraryUserId);
    }

    public int decrementBoardCount(Long libraryUserId) {
        return libraryRepository.decrementBoardCount(libraryUserId);
    }

    // 서재 업데이트
    public void initLibrary(Long userId) {
        libraryRepository.save(new Library(userId));
    }

    public void deleteLibrary(Long userId) {
        libraryRepository.deleteById(userId);
    }

    // 서재 정보 조회
    public int findReviewCount(Long userId) {
        return libraryRepository.findReviewCountByUserId(userId);
    }

}
