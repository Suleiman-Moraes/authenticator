package com.moraes.authenticator.api.mock.real_state;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public EnterpriseDTO mockEnterpriseDTO(int number) {
        try {
            EnterpriseDTO entity = new EnterpriseDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
