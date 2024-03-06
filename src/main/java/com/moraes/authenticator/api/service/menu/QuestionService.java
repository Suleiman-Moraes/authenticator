package com.moraes.authenticator.api.service.menu;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
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
        entity.setOrder(Optional.ofNullable(entity.getOrder())
                .orElseGet(() -> getNextOrder(entity.getCompany().getKey(), entity.getTypeFrom())));
        repository.save(entity);
        return entity.getKey();
    }

    @Transactional
    @Override
    public List<Long> insertAll(List<QuestionAllDTO> dtos, TypeFromEnum typeFrom) {
        AtomicInteger atomicOrder = new AtomicInteger();
        atomicOrder.set(getNextOrder(userService.getMe().getCompany().getKey(), typeFrom));
        return dtos.stream().map(dto -> {
            if (dto == null) {
                return null;
            }
            Question question = Mapper.parseObject(dto, Question.class);
            question.setOrder(atomicOrder.getAndIncrement());
            question.setTypeFrom(typeFrom);
            return insert(question);
        }).toList();
    }

    @Transactional
    @Override
    public Long update(QuestionDTO dto, Long key) {
        final Question entity = Mapper.parseObjectForUpdate(dto, findByKey(key));
        valid(entity);
        repository.save(entity);
        return key;
    }

    public void valid(Question entity) {
        final Long key = Optional.ofNullable(entity.getKey()).orElse(0L);
        final Long companyKey = entity.getCompany().getKey();
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(!repository.existsByKeyNotAndCompanyKeyAndTypeFromAndValueIgnoreCase(key, companyKey,
                                entity.getTypeFrom(),
                                entity.getValue()))
                        .messageKey("question.company_typeFrom_value.duplicate")
                        .messageParams(ExceptionUtilDTO.messageParamsBuild(entity.getValue()))
                        .build(),
                ExceptionUtilDTO.builder()
                        .condition(!repository.existsByKeyNotAndCompanyKeyAndTypeFromAndOrder(key, companyKey,
                                entity.getTypeFrom(),
                                entity.getOrder()))
                        .messageKey("question.company_typeFrom_order.duplicate")
                        .build());
    }

    public int getNextOrder(Long companyKey, TypeFromEnum typeFrom) {
        return repository.findMaxOrderByCompanyKeyAndTypeFrom(companyKey, typeFrom).orElse(0) + 1;
    }
}
