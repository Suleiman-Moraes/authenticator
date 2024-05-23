package com.moraes.authenticator.api.mock.real_state;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.MockCompany;
import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.real_state.construction.ConstructionDTO;
import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockConstruction extends AbstractMock<Construction> {

    public static final String INTEGRATION_NAME = "Construction Name Integration Test";

    @Setter
    @Getter
    private int maxSize = 14;

    @Override
    protected Class<Construction> getClazz() {
        return Construction.class;
    }

    @Override
    protected void setOdersValues(Construction entity, Integer number) {
        entity.setCompany(new MockCompany().mockEntity(number));
    }

    public List<String> mockNameList(int size) {
        List<String> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add("Teste String" + i);
        }
        return entitys;
    }

    public List<ConstructionDTO> mockConstructionDTOList(int size) {
        List<ConstructionDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add(mockConstructionDTO(i));
        }
        return entitys;
    }

    public ConstructionDTO mockConstructionDTO(Integer number) {
        try {
            ConstructionDTO entity = new ConstructionDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public ConstructionDTO mockIntegrationConstructionDTO() {
        return ConstructionDTO.builder()
                .name(INTEGRATION_NAME)
                .build();
    }
}
