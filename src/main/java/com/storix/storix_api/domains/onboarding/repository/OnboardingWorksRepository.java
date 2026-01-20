package com.storix.storix_api.domains.onboarding.repository;

import com.storix.storix_api.domains.onboarding.domain.OnboardingWorks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface OnboardingWorksRepository extends JpaRepository<OnboardingWorks, Long> {

    @Query("SELECT COUNT(o) " +
            "FROM OnboardingWorks o " +
            "WHERE o.worksId IN :worksIds")
    long countByWorksIds(@Param("worksIds") Set<Long> worksIds);

}
