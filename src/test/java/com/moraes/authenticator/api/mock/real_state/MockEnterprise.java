package com.moraes.authenticator.api.mock.real_state;

import java.math.BigDecimal;
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

    public static final String INTEGRATION_NAME = "Enterprise Name Integration Test";
    public static final String INTEGRATION_UNIT = "Enterprise Unit Integration Test";

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

    public EnterpriseDTO mockEnterpriseDTOWrongValues() {
        return EnterpriseDTO.builder()
                .value(BigDecimal.valueOf(-1))
                .vpl(BigDecimal.ZERO)
                .valueM2(BigDecimal.ZERO)
                .sizeM2(BigDecimal.ZERO)
                .name(" ")
                .unit(MockUtil.getStringBySize(101))
                .constructionName(MockUtil.getStringBySize(101))
                .build();
    }

    public EnterpriseDTO mockIntegrationEnterpriseDTO() {
        try {
            EnterpriseDTO entity = new EnterpriseDTO();
            MockUtil.toFill(entity, 99, ignoreFields);

            entity.setName(INTEGRATION_NAME);
            entity.setUnit(INTEGRATION_UNIT);
            // setOdersValues
            entity.setConstructionName(MockConstruction.INTEGRATION_NAME);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}