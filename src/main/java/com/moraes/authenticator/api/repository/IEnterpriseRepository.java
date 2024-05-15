package com.moraes.authenticator.api.repository;

import java.util.List;
import java.util.Optional;

import com.moraes.authenticator.api.model.real_state.Enterprise;

public interface IEnterpriseRepository extends IExtendedRepository<Enterprise, Long> {

        List<String> findDistinctNameByConstructionNameAndConstructionCompanyKeyAndEnabledTrueOrderByName(
                        String constructionName, Long companyKey);

        Optional<Enterprise> findTopByNameAndConstructionNameAndConstructionCompanyKeyAndEnabledTrue(String name,
                        String constructionName, Long companyKey);
}
