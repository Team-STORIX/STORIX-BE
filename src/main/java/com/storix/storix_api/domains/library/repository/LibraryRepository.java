package com.storix.storix_api.domains.library.repository;

import com.storix.storix_api.domains.library.domain.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}
