package com.moraes.authenticator.api.mock.real_state;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.enums.FrequencyEnum;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockCondition extends AbstractMock<Condition> {

    @Setter
    @Getter
    private int maxSize = 14;

    @Override
    protected Class<Condition> getClazz() {
        return Condition.class;
    }

    @Override
    protected void setOdersValues(Condition entity, Integer number) {
        entity.setProposal(new MockProposal().mockEntity(number));
        entity.setFrequency(FrequencyEnum.FINANCING);
    }

    public ConditionDTO mockConditionDTO(int number) {
        try {
            ConditionDTO entity = new ConditionDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setFrequency(FrequencyEnum.FINANCING);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<ConditionDTO> mockConditionDTOList() {
        return mockConditionDTOList(maxSize);
    }

    public List<ConditionDTO> mockConditionDTOList(int size) {
        List<ConditionDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add(mockConditionDTO(i));
        }
        return entitys;
    }
}
