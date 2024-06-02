package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.real_state.MockProposal;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IProposalRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConditionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;

class ProposalServiceTest {

    private static final Long KEY = 1L;

    private MockProposal input;
    private MockSecurity mockSecurity;

    @Spy
    @InjectMocks
    private ProposalService service;

    @Mock
    private IProposalRepository repository;

    @Mock
    private IEnterpriseService enterpriseService;

    @Mock
    private IConditionService conditionService;

    private Proposal entity;

    @BeforeEach
    void setUp() {
        input = new MockProposal();
        mockSecurity = new MockSecurity();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testGetRepository() {
        assertNotNull(service.getRepository(), "Repository is null");
    }

    @Test
    @DisplayName("Junit Test Given Proposal When Insert Then Return Long Key")
    void testGivenProposalWhenInsertThenReturnLongKey() {
        final ProposalDTO dto = input.mockProposalDTO(1);

        assertDoesNotThrow(() -> service.insert(dto), "Does Not Throw");

        ArgumentCaptor<Proposal> captor = ArgumentCaptor.forClass(Proposal.class);
        verify(repository, times(1)).save(captor.capture());
        final Proposal captured = captor.getValue();

        assertEquals(dto.getValue(), captured.getValue(), "Value not equal");
        assertEquals(dto.getVpl(), captured.getVpl(), "Vpl not equal");
        assertEquals(dto.getValueM2(), captured.getValueM2(), "ValueM2 not equal");
        assertEquals(dto.getSizeM2(), captured.getSizeM2(), "SizeM2 not equal");
        assertEquals(dto.getConditions().size(), captured.getConditions().size(), "Conditions size not equal");
    }

    @Test
    void testFindPageAll() {
        final int maxSize = 10;
        final ProposalFilterDTO filter = new ProposalFilterDTO();
        final List<ProposalListDTO> list = input.mockProposalListDTOListWithKey(maxSize);
        final Page<ProposalListDTO> page = new PageImpl<>(list);
        mockSecurity.mockSuperUser();
        when(repository.page(eq(filter), any(), eq(ProposalListDTO.class), eq(Proposal.class), anyString(), any()))
                .thenReturn(page);

        final Page<ProposalListDTO> pages = service.findPageAll(filter);
        assertNotNull(pages, "Return null");
        for (int index = 1; index <= maxSize; index++) {
            final var dto = pages.getContent().get(index - 1);
            final var entity = list.get(index - 1);
            assertEquals(dto.getLinks().toList().get(0).getHref(), "/api/v1/proposal/" + index,
                    "Href not equal");
            assertEquals(dto.getKey(), entity.getKey(), "Key not equal");
            assertEquals(dto.getEnterpriseName(), entity.getEnterpriseName(), "EnterpriseName not equal");
            assertEquals(dto.getEnterpriseUnit(), entity.getEnterpriseUnit(), "EnterpriseUnit not equal");
            assertEquals(dto.getSizeM2(), entity.getSizeM2(), "SizeM2 not equal");
            assertEquals(dto.getValue(), entity.getValue(), "Value not equal");
            assertEquals(dto.getDate(), entity.getDate(), "Date not equal");
        }
    }
}
