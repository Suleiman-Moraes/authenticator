package com.moraes.authenticator.api.mock.real_state;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.enums.FrequencyEnum;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.util.MockUtil;
import com.moraes.authenticator.api.util.Util;

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
            
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 05);
            entity.setBeginningInstallment(Util.calendarToLocalDate(calendar));

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

    public ConditionDTO mockConditionDTOWrongValues() {
        return ConditionDTO.builder()
                .numberInstallments(0)
                .valueInstallments(BigDecimal.ZERO)
                .beginningInstallment(LocalDate.of(1990, 1, 18))
                .build();
    }

    /*
     * @NotNull
     * private FrequencyEnum frequency;
     * 
     * @NotNull
     * 
     * @Positive
     * private Integer numberInstallments;
     * 
     * @Positive
     * 
     * @NotNull
     * private BigDecimal valueInstallments;
     * 
     * @NotNull
     * 
     * @FutureOrPresent
     * 
     * @IDayValidBetween5O10O15
     * private LocalDate beginningInstallment;
     */
}
