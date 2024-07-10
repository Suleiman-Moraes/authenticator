package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.mock.real_state.MockCondition;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.real_state.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IConditionMapperTest {

	@Spy
	@InjectMocks
	private IConditionMapper mapper = Mappers.getMapper(IConditionMapper.class);

	private MockCondition input;

	private static final Long KEY = 1L;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		input = new MockCondition();
	}

	@Test
	@DisplayName("JUnit test given condition and conditionDTO when updateFromConditionDTO then update condition")
	void testGivenConditionAndConditionDTOWhenUpdateFromConditionDTOThenUpdateCondition() {
		Condition condition = input.mockEntity(1);
		condition.setKey(KEY);
		condition.setProposal(null);
		ConditionDTO conditionDTO = input.mockConditionDTO(2);

		mapper.updateFromConditionDTO(condition, conditionDTO);

		assertEquals(conditionDTO.getFrequency(), condition.getFrequency(), "Frequency is different");
		assertEquals(conditionDTO.getNumberInstallments(), condition.getNumberInstallments(),
				"NumberInstallments is different");
		assertEquals(conditionDTO.getValueInstallments().doubleValue(), condition.getValueInstallments().doubleValue(),
				"ValueInstallments is different");
		assertEquals(conditionDTO.getBeginningInstallment(), condition.getBeginningInstallment(),
				"BeginningInstallment is different");
		assertNull(condition.getProposal(), "Proposal is not null");
	}
}