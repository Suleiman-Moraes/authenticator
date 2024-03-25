package com.moraes.authenticator.api.service.menu;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.controller.QuestionController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionFilterDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.enums.TypeEnum;
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
        entity.setCompany(userService.getMe().getCompany());
        valid(entity);
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
        final Question entity = Mapper.parseObjectForUpdate(dto, findByKeyAndCompanyKey(key));
        valid(entity);
        repository.save(entity);
        return key;
    }

    @Transactional
    @Override
    public void delete(Long key) {
        Question entity = findByKeyAndCompanyKey(key);
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(CollectionUtils.isEmpty(entity.getAnswers()))
                        .messageKey("question.answers.delete_error")
                        .build());
        repository.delete(entity);
    }

    @Transactional(readOnly = true)
    @Override
    public Question findByKeyAndCompanyKey(Long key) {
        return repository.findByKeyAndUserCompanyKey(key, userService.getMe().getCompany().getKey())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<QuestionListDTO> findPageAll(QuestionFilterDTO filter) {
        final Map<String, Class<?>> fields = getMapOfFields();
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("companyKey", userService.getMe().getCompany().getKey());
        Page<QuestionListDTO> page = repository.page(filter, fields, QuestionListDTO.class, Question.class,
                "x.company.key = :companyKey", parameters);
        page.getContent().forEach(dto -> addLinks(dto, (long) dto.getKey(), QuestionController.class));
        return page;
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

    private Map<String, Class<?>> getMapOfFields() {
        final Map<String, Class<?>> fields = new LinkedHashMap<>();
        fields.put("x.key", Number.class);
        fields.put("x.value", String.class);
        fields.put("x.mask", String.class);
        fields.put("x.order", Integer.class);
        fields.put("x.typeFrom", TypeFromEnum.class);
        fields.put("x.type", TypeEnum.class);
        fields.put("x.enabled", Boolean.class);
        fields.put("x.required", Boolean.class);
        return fields;
    }
}
