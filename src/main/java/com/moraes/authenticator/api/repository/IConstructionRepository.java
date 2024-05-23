package com.moraes.authenticator.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.moraes.authenticator.api.model.real_state.Construction;

import jakarta.annotation.Nullable;

public interface IConstructionRepository extends IExtendedRepository<Construction, Long> {

    @Query("SELECT c.name FROM Construction c WHERE c.company.key = :companyKey AND c.enabled = true ORDER BY c.name")
    List<String> findNameByCompanyKeyAndEnabledTrueOrderByName(Long companyKey);

    Optional<Construction> findByNameAndCompanyKey(String name, Long companyKey);

    boolean existsByKeyNotAndCompanyKeyAndName(@Nullable Long key, Long companyKey, String name);
}
