package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.real_state.MockProposal;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IProposalRepository;

class ProposalServiceTest {

    private static final Long KEY = 1L;

    private MockProposal input;

    @Spy
    @InjectMocks
    private ProposalService service;

    @Mock
    private IProposalRepository repository;

    private Proposal entity;

    @BeforeEach
    void setUp() {
        input = new MockProposal();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testGetRepository() {
        assertNotNull(service.getRepository(), "Repository is null");
    }
}
