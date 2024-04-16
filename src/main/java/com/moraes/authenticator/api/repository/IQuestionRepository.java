package com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;

public interface IQuestionRepository extends IExtendedRepository<Question, Long> {

    boolean existsByKeyNotAndCompanyKeyAndTypeFromAndValueIgnoreCase(Long key, Long companyKey,
            TypeFromEnum typeFrom,
            String value);

    boolean existsByKeyNotAndCompanyKeyAndTypeFromAndOrder(Long key, Long companyKey, TypeFromEnum typeFrom,
            Integer order);

    @Query("SELECT MAX(q.order) FROM Question q WHERE q.company.key = :companyKey AND q.typeFrom = :typeFrom")
    Optional<Integer> findMaxOrderByCompanyKeyAndTypeFrom(@Param("companyKey") Long companyKey,
            @Param("typeFrom") TypeFromEnum typeFrom);

    Optional<Question> findByKeyAndCompanyKey(Long key, Long companyKey);
}
