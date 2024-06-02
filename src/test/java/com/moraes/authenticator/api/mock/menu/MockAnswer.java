package com.moraes.authenticator.api.mock.menu;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.menu.Answer;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.SneakyThrows;

public class MockAnswer extends AbstractMock<Answer> {

    @Override
    protected Class<Answer> getClazz() {
        return Answer.class;
    }

    @Override
    protected void setOdersValues(Answer entity, Integer number) {
        entity.setPerson(new MockPerson().mockEntity(number));
        entity.setQuestion(new MockQuestion().mockEntity(number));
    }

    @SneakyThrows
    public List<Answer> mockEntityList(Integer number, Integer size) {
        List<Answer> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            Answer entity = new Answer();
            MockUtil.toFill(entity, i, ignoreFields);
            setOdersValues(entity, number);
            entitys.add(entity);
        }
        return entitys;
    }
}
