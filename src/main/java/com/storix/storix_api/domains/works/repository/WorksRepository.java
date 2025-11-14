package com.storix.storix_api.domains.works.repository;

import com.storix.storix_api.domains.works.domain.Works;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorksRepository extends JpaRepository<Works, Long> {
}
