package com.moraes.authenticator.api.repository;

import java.util.Optional;

import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;

public interface IQuestionRepository extends IExtendedRepository<Question, Long> {

    boolean existsByKeyNotAndCompanyKeyAndTypeFromAndValueIgnoreCase(Long key, Long companyKey, TypeFromEnum typeFrom,
            String value);

    boolean existsByKeyNotAndCompanyKeyAndTypeFromAndOrder(Long key, Long companyKey, TypeFromEnum typeFrom,
            Integer order);

    Optional<Integer> findMaxOrderByCompanyKeyAndTypeFrom(Long companyKey, TypeFromEnum typeFrom);

    Optional<Question> findByKeyAndUserCompanyKey(Long key, Long companyKey);
}
