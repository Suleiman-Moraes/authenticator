package com.moraes.authenticator.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.moraes.authenticator.api.model.real_state.Enterprise;

public interface IEnterpriseRepository extends IExtendedRepository<Enterprise, Long> {

    @Query("SELECT DISTINCT e.name FROM Enterprise e WHERE e.construction.name = ?1 AND e.construction.company.key = ?2 AND e.enabled = true ORDER BY e.name")
    List<String> findDistinctNameByConstructionNameAndConstructionCompanyKeyAndEnabledTrueOrderByName(
            String constructionName, Long companyKey);

    Optional<Enterprise> findTopByNameAndConstructionNameAndConstructionCompanyKeyAndEnabledTrue(String name,
            String constructionName, Long companyKey);
}
