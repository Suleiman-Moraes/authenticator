package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.mock.real_state.MockProposal;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.*;

class IProposalMapperTest {

	@Spy
	@InjectMocks
	private IProposalMapper mapper = Mappers.getMapper(IProposalMapper.class);

	private MockProposal input;

	private static final Long KEY = 1L;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		input = new MockProposal();
	}

	@Test
	@DisplayName("JUnit test given proposal and proposalDTO when updateFromProposalDTO then update proposal")
	void testGivenProposalAndProposalDTOWhenUpdateFromProposalDTOThenUpdateProposal() {
		Proposal proposal = input.mockEntity(1);
		proposal.setKey(KEY);
		proposal.setEnterprise(null);
		proposal.setConditions(null);
		ProposalDTO proposalDTO = input.mockProposalDTO(2);

		mapper.updateFromProposalDTO(proposal, proposalDTO);

		assertEquals(proposalDTO.getValue().doubleValue(), proposal.getValue().doubleValue(), "Value is different");
		assertEquals(proposalDTO.getVpl().doubleValue(), proposal.getVpl().doubleValue(), "Vpl is different");
		assertEquals(proposalDTO.getValueM2().doubleValue(), proposal.getValueM2().doubleValue(),
				"ValueM2 is different");
		assertEquals(proposalDTO.getSizeM2().doubleValue(), proposal.getSizeM2().doubleValue(), "SizeM2 is different");
		assertEquals(KEY, proposal.getKey(), "Key is different");
		assertNull(proposal.getEnterprise(), "Enterprise is not null");
		assertNull(proposal.getConditions(), "Conditions is not null");
	}
}
