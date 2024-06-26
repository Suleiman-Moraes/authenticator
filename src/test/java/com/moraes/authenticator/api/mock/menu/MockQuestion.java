package com.moraes.authenticator.api.mock.menu;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.MockCompany;
import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionListDTO;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockQuestion extends AbstractMock<Question> {

    @Setter
    @Getter
    private int maxSize = 14;

    @Override
    protected Class<Question> getClazz() {
        return Question.class;
    }

    @Override
    protected void setOdersValues(Question entity, Integer number) {
        entity.setCompany(new MockCompany().mockEntity(number));
        entity.setType(TypeEnum.TEXT);
        entity.setTypeFrom(TypeFromEnum.PERSON);
    }

    public Question mockEntityWithAnswers(Integer number) {
        Question entity = mockEntity(number);
        entity.setAnswers(new MockAnswer().mockEntityList(number, 5));
        return entity;
    }

    public QuestionAllDTO mockQuestionAllDTO(Integer number) {
        try {
            QuestionAllDTO entity = new QuestionAllDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setType(TypeEnum.TEXT);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<QuestionAllDTO> mockQuestionAllDTOList(int size) {
        List<QuestionAllDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add(mockQuestionAllDTO(i));
        }
        return entitys;
    }

    public QuestionDTO mockQuestionDTO(int number) {
        try {
            QuestionDTO entity = new QuestionDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setType(TypeEnum.TEXT);
            entity.setTypeFrom(TypeFromEnum.PERSON);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<QuestionListDTO> mockQuestionListDTOListWithKey(int maxSize) {
        List<QuestionListDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= maxSize; i++) {
            QuestionListDTO entity = mockQuestionListDTO(i);
            entity.setKey(Integer.valueOf(i).longValue());
            entitys.add(entity);
        }
        return entitys;
    }

    private QuestionListDTO mockQuestionListDTO(int number) {
        try {
            QuestionListDTO entity = new QuestionListDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setType(TypeEnum.TEXT);
            entity.setTypeFrom(TypeFromEnum.PERSON);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public QuestionDTO mockQuestionDTOWrongValues() {
        return QuestionDTO.builder()
                .value(MockUtil.getStringBySize(256))
                .mask(MockUtil.getStringBySize(51))
                .order(0)
                .type(TypeEnum.TEXT)
                .typeFrom(TypeFromEnum.PERSON)
                .build();
    }

    public List<QuestionListDTO> mockQuestionListDTOList(int maxSize) {
        List<QuestionListDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= maxSize; i++) {
            entitys.add(mockQuestionListDTO(i));
        }
        return entitys;
    }
}
