package com.moraes.authenticator.api.mock.menu;

import com.moraes.authenticator.api.mock.MockCompany;
import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;
import com.moraes.authenticator.api.model.menu.Question;

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
}
