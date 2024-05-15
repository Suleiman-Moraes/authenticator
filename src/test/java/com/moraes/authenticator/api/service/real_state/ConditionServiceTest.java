package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.real_state.MockCondition;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.repository.IConditionRepository;

class ConditionServiceTest {

    private static final Long KEY = 1L;

    private MockCondition input;

    @Spy
    @InjectMocks
    private ConditionService service;

    @Mock
    private IConditionRepository repository;

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
        final ConditionDTO dto = input.mockConditionDTO(1);

        when(repository.save(any())).thenReturn(entity);

        assertDoesNotThrow(() -> service.insert(dto, proposalKey), "Does Not Throw");

        ArgumentCaptor<Condition> conditionCaptor = ArgumentCaptor.forClass(Condition.class);
        verify(service, times(1)).save(conditionCaptor.capture());
        final Condition capturedCondition = conditionCaptor.getValue();

        assertNotNull(capturedCondition.getProposal(), "Proposal is null");
        assertEquals(proposalKey, capturedCondition.getProposal().getKey(), "ProposalKey not equal");
        assertEquals(dto.getBeginningInstallment(), capturedCondition.getBeginningInstallment(),
                "BeginningInstallment not equal");
        assertEquals(dto.getFrequency(), capturedCondition.getFrequency(), "Frequency not equal");
        assertEquals(dto.getNumberInstallments(), capturedCondition.getNumberInstallments(),
                "NumberInstallments not equal");
        assertEquals(dto.getValueInstallments(), capturedCondition.getValueInstallments(),
                "ValueInstallments not equal");
    }

    @Test
    @DisplayName("Junit Test Given List of ConditionDTO And ProposalKey When Insert All Then Save Condition")
    void testGivenListOfConditionDTOAndProposalKeyWhenInsertAllThenSaveCondition() {
        final Long proposalKey = 1L;
        List<ConditionDTO> dtos = input.mockConditionDTOList(10);
        dtos.set(4, null);
        dtos.set(6, null);

        when(repository.save(any())).thenReturn(entity);

        assertDoesNotThrow(() -> service.insertAll(dtos, proposalKey), "Does Not Throw");
        verify(service, times(8)).insert(any(), eq(proposalKey));
    }

    @Test
    @DisplayName("Junit Test Given null List of ConditionDTO And ProposalKey When Insert All Then Do Not Save Condition")
    void testGivenNullListOfConditionDTOAndProposalKeyWhenInsertAllThenDoNotSaveCondition() {
        final Long proposalKey = 1L;
        final List<ConditionDTO> dtos = null;

        assertDoesNotThrow(() -> service.insertAll(dtos, proposalKey), "Does Not Throw");
        verify(service, times(0)).insert(any(), eq(proposalKey));
    }
}
