package com.moraes.authenticator.api.service.menu;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.repository.IQuestionRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;
import com.moraes.authenticator.api.util.ExceptionsUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class QuestionService implements IQuestionService {

    @Getter
    private IQuestionRepository repository;

    private IUserService userService;

    @Transactional
    @Override
    public Long insert(Question entity) {
        valid(entity);
        entity.setCompany(userService.getMe().getCompany());
        repository.save(entity);
        return entity.getKey();
    }

    public void valid(Question entity) {
        final Long id = Optional.ofNullable(entity.getKey()).orElse(0L);
        final Long companyKey = entity.getCompany().getKey();
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(!repository.existsByKeyNotAndCompanyKeyAndTypeFromAndValueIgnoreCase(id, companyKey,
                                entity.getTypeFrom(),
                                entity.getValue()))
                        .messageKey("question.company_typeFrom_value.duplicate")
                        .build(),
                ExceptionUtilDTO.builder()
                        .condition(!repository.existsByKeyNotAndCompanyKeyAndTypeFromAndOrder(id, companyKey,
                                entity.getTypeFrom(),
                                entity.getOrder()))
                        .messageKey("question.company_typeFrom_order.duplicate")
                        .build());
    }
}
