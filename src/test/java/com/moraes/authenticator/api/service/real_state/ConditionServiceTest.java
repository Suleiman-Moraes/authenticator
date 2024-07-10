package com.moraes.authenticator.api.service.real_state;

import com.moraes.authenticator.api.mapper.IConditionMapper;
import com.moraes.authenticator.api.mock.real_state.MockCondition;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.enums.OperationModifyEnum;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.repository.IConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ConditionServiceTest {

	private static final Long KEY = 1L;

	private MockCondition input;

	@Spy
	@InjectMocks
	private ConditionService service;

	@Mock
	private IConditionRepository repository;

	@Mock
	private IConditionMapper conditionMapper;

	private Condition entity;

	@BeforeEach
	void setUp() {
		input = new MockCondition();
		MockitoAnnotations.openMocks(this);

		entity = input.mockEntity(1);
		entity.setKey(KEY);
	}

	@Test
	void testGetRepository() {
		assertNotNull(service.getRepository(), "Repository is null");
	}

	@Test
	@DisplayName("Junit Test Given Condition When Save Then Return Saved Condition Key")
	void testGivenConditionWhenSaveThenReturnSavedConditionKey() {
		when(repository.save(any())).thenReturn(entity);
		entity.setAmount(BigDecimal.ZERO);
		entity.setNumberInstallments(2);
		entity.setValueInstallments(BigDecimal.TEN);

		final Long key = service.save(entity);

		assertEquals(KEY, key, "Key not equal");
		assertEquals(BigDecimal.valueOf(20), entity.getAmount(), "Amount not equal");
	}

	@Test
	@DisplayName("Junit Test Given Condition And ProposalKey When Insert Then Save Condition")
	void testGivenConditionAndProposalKeyWhenInsertThenSaveCondition() {
		final Long proposalKey = 1L;
		final Condition condition = input.mockEntity(1);

		when(repository.save(any())).thenReturn(entity);

		assertDoesNotThrow(() -> service.insert(condition, proposalKey), "Does Not Throw");

		ArgumentCaptor<Condition> conditionCaptor = ArgumentCaptor.forClass(Condition.class);
		verify(service, times(1)).save(conditionCaptor.capture());
		final Condition capturedCondition = conditionCaptor.getValue();

		assertNotNull(capturedCondition.getProposal(), "Proposal is null");
		assertEquals(proposalKey, capturedCondition.getProposal().getKey(), "ProposalKey not equal");
		assertEquals(condition.getBeginningInstallment(), capturedCondition.getBeginningInstallment(),
				"BeginningInstallment not equal");
		assertEquals(condition.getFrequency(), capturedCondition.getFrequency(), "Frequency not equal");
		assertEquals(condition.getNumberInstallments(), capturedCondition.getNumberInstallments(),
				"NumberInstallments not equal");
		assertEquals(condition.getValueInstallments(), capturedCondition.getValueInstallments(),
				"ValueInstallments not equal");
	}

	@Test
	@DisplayName("Junit Test Given List of Condition And ProposalKey When Insert All Then Save Condition")
	void testGivenListOfConditionAndProposalKeyWhenInsertAllThenSaveCondition() {
		final Long proposalKey = 1L;
		List<Condition> conditions = input.mockEntityList(10);
		conditions.set(4, null);
		conditions.set(6, null);

		when(repository.save(any())).thenReturn(entity);

		assertDoesNotThrow(() -> service.insertAll(conditions, proposalKey), "Does Not Throw");
		verify(service, times(8)).insert(any(), eq(proposalKey));
	}

	@Test
	@DisplayName("Junit Test Given null List of Condition And ProposalKey When Insert All Then Do Not Save Condition")
	void testGivenNullListOfConditionAndProposalKeyWhenInsertAllThenDoNotSaveCondition() {
		final Long proposalKey = 1L;

		assertDoesNotThrow(() -> service.insertAll(null, proposalKey), "Does Not Throw");
		verify(service, times(0)).insert(any(), eq(proposalKey));
	}

	@Test
	@DisplayName("Junit Test Given Null List of Condition When Update All Then Throw Exception")
	void testGivenNullListOfConditionAndProposalKeyWhenUpdateAllThenThrowException() {
		assertThrows(NullPointerException.class, () -> service.updateAll(null), "Throw Exception");
	}

	@ParameterizedTest(name = "{index} => conditions={0}, quantity={1}")
	@MethodSource("provideParametersListOfConditionAndQuantity")
	@DisplayName("Junit Test Given List of Condition When Update All Then Update Conditions")
	void testGivenListOfConditionAndProposalKeyWhenUpdateAllThenUpdateCondition(List<Condition> conditions,
			int quantity) {
		when(repository.save(any())).thenReturn(entity);

		assertDoesNotThrow(() -> service.updateAll(conditions), "Does Not Throw");
		verify(service, times(quantity)).save(any());
	}

	@Test
	@DisplayName("Junit Test Given Null List of Condition When deleteAll Then Throw Exception")
	void testGivenNullListOfConditionAndProposalKeyWhenDeleteAllThenThrowException() {
		assertThrows(NullPointerException.class, () -> service.deleteAll(null), "Throw Exception");
	}

	@ParameterizedTest(name = "{index} => conditions={0}, quantity={1}")
	@MethodSource("provideParametersListOfConditionAndQuantity")
	@DisplayName("Junit Test Given List of Condition When deleteAll Then Delete Conditions")
	void testGivenListOfConditionAndProposalKeyWhenDeleteAllThenDeleteCondition(List<Condition> conditions,
			int quantity) {
		assertDoesNotThrow(() -> service.deleteAll(conditions), "Does Not Throw");
		verify(repository, times(quantity)).deleteById(any());
	}

	@ParameterizedTest(name = "{index} => conditions={0}, conditionDTOs={1}, quantityInsert={2}, quantityUpdate={3}, quantityDelete={4}")
	@MethodSource("provideParametersListOfConditionAndListOfConditionDtoAndQuantities")
	@DisplayName("Junit Test Given List of Condition And List of ConditionDTO When carryOutTheSeparation Then return Map with condition operations")
	void testGivenListOfConditionAndListOfConditionDTOWhenCarryOutTheSeparationThenReturnMapWithConditionOperations(
			List<Condition> conditions, List<ConditionDTO> conditionDTOs, int quantityInsert, int quantityUpdate,
			int quantityDelete) {
		final Map<OperationModifyEnum, List<Condition>> map = service.carryOutTheSeparation(conditions, conditionDTOs);

		assertEquals(quantityInsert, map.get(OperationModifyEnum.INSERT).size(), "QuantityInsert not equal");
		assertEquals(quantityUpdate, map.get(OperationModifyEnum.UPDATE).size(), "QuantityUpdate not equal");
		assertEquals(quantityDelete, map.get(OperationModifyEnum.DELETE).size(), "QuantityDelete not equal");
	}

	//void updateAll(List<Condition> conditions, List<ConditionDTO> conditionDTOs, Long proposalKey)
	@ParameterizedTest(name = "{index} => conditions={0}, conditionDTOs={1}, quantityInsert={2}, quantityUpdate={3}, quantityDelete={4}")
	@MethodSource("provideParametersListOfConditionAndListOfConditionDtoAndQuantities")
	@DisplayName("Junit Test Given List of Condition And List of ConditionDTO And proposalKey When updateAll Then update Conditions")
	void testGivenListOfConditionAndListOfConditionDTOAndProposalKeyWhenUpdateAllThenUpdateCondition(
			List<Condition> conditions, List<ConditionDTO> conditionDTOs, int quantityInsert, int quantityUpdate,
			int quantityDelete) {
		when(repository.save(any())).thenReturn(entity);

		assertDoesNotThrow(() -> service.updateAll(conditions, conditionDTOs, KEY), "Does Not Throw");

		verify(service, times(quantityInsert > 0 ? 1 : 0)).insertAll(any(), eq(KEY));
		verify(service, times(quantityUpdate > 0 ? 1 : 0)).updateAll(any());
		verify(service, times(quantityDelete > 0 ? 1 : 0)).deleteAll(any());
	}

	private static Stream<Arguments> provideParametersListOfConditionAndListOfConditionDtoAndQuantities() {
		final MockCondition input = new MockCondition();
		final List<Condition> conditionsA = input.mockEntityList(10);
		final List<ConditionDTO> conditionDtosA = input.mockConditionDTOList(10);

		final List<Condition> conditionsB = input.mockEntityList(10);
		final List<ConditionDTO> conditionDtosB = input.mockConditionDTOList(5);

		final List<Condition> conditionsC = input.mockEntityList(5);
		final List<ConditionDTO> conditionDtosC = input.mockConditionDTOList(10);

		final List<Condition> conditionsD = new ArrayList<>(0);
		final List<ConditionDTO> conditionDtosD = new ArrayList<>(0);

		final List<Condition> conditionsE = new ArrayList<>(0);
		final List<ConditionDTO> conditionDtosE = input.mockConditionDTOList(5);

		final List<Condition> conditionsF = input.mockEntityList(5);
		final List<ConditionDTO> conditionDtosF = new ArrayList<>(0);

		return Stream.of(Arguments.of(conditionsA, conditionDtosA, 0, 10, 0),
				Arguments.of(conditionsB, conditionDtosB, 0, 5, 5), Arguments.of(conditionsC, conditionDtosC, 5, 5, 0),
				Arguments.of(conditionsD, conditionDtosD, 0, 0, 0), Arguments.of(conditionsE, conditionDtosE, 5, 0, 0),
				Arguments.of(conditionsF, conditionDtosF, 0, 0, 5));
	}

	private static Stream<Arguments> provideParametersListOfConditionAndQuantity() {
		final List<Condition> conditions = new MockCondition().mockEntityList(10);

		return Stream.of(Arguments.of(conditions, 10), Arguments.of(new ArrayList<>(0), 0));
	}
}
