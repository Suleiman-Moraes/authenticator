package com.moraes.authenticator.api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.real_state.Construction;

import jakarta.annotation.Nullable;

public interface IConstructionRepository extends JpaRepository<Construction, Long> {

    List<String> findNameByCompanyKeyAndEnabledTrueOrderByName(Long companyKey);

    Optional<Construction> findByNameAndCompanyKey(String name, Long companyKey);

    boolean existsByIdNotAndCompanyKeyAndName(@Nullable Long key, Long companyKey, String name);
}
