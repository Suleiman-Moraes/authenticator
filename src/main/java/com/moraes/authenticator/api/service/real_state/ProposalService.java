package com.moraes.authenticator.api.service.real_state;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.repository.IProposalRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ProposalService implements IProposalService{
    
    @Getter
    private IProposalRepository repository;
}
