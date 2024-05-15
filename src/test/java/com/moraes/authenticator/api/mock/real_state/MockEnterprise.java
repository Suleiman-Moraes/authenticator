package com.moraes.authenticator.api.mock.real_state;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.real_state.Enterprise;

import lombok.Getter;
import lombok.Setter;

public class MockEnterprise extends AbstractMock<Enterprise> {

    @Setter
    @Getter
    private int maxSize = 14;

    @Override
    protected Class<Enterprise> getClazz() {
        return Enterprise.class;
    }

    @Override
    protected void setOdersValues(Enterprise entity, Integer number) {
        entity.setConstruction(new MockConstruction().mockEntity(number));
    }

    public List<String> mockNameList(int size) {
        List<String> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add("Teste String" + i);
        }
        return entitys;
    }
}
