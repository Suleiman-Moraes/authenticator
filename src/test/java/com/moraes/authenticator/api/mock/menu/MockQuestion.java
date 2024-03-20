package com.moraes.authenticator.api.mock.menu;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.MockCompany;
import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionAllDTO;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockQuestion extends AbstractMock<Question> {

    @Override
    protected Class<Question> getClazz() {
        return Question.class;
    }

    @Override
    protected void setOdersValues(Question entity, Integer number) {
        entity.setCompany(new MockCompany().mockEntity(number));
        entity.setTypeFrom(TypeFromEnum.PERSON);
    }

    public Question mockEntityWithAnswers(Integer number){
        Question entity = mockEntity(number);
        entity.setAnswers(new MockAnswer().mockEntityList(number, 5));
        return entity;
    }

    public QuestionAllDTO mockQuestionAllDTO(Integer number) {
        try {
            QuestionAllDTO entity = new QuestionAllDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
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
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
