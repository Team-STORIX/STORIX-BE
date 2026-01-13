package com.storix.storix_api.domains.library.adaptor;

import com.storix.storix_api.domains.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LibraryAdaptor {

    private final LibraryRepository libraryRepository;

    public void incrementReviewCount(Long libraryUserId) {
        libraryRepository.incrementReviewCount(libraryUserId);
    }

    public int decrementReviewCount(Long libraryUserId) {
        return libraryRepository.decrementReviewCount(libraryUserId);
    }

}
