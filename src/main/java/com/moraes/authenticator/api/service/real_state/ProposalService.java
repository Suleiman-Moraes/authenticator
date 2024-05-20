package com.moraes.authenticator.api.service.real_state;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IProposalRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConditionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ProposalService implements IProposalService {

    @Getter
    private IProposalRepository repository;

    private final IEnterpriseService enterpriseService;

    private final IConditionService conditionService;

    @Override
    public Long insert(ProposalDTO proposalDTO) {
        Proposal proposal = Mapper.parseObject(proposalDTO, Proposal.class);
        proposal.getEnterprise().setKey(
                enterpriseService.insert(proposal.getEnterprise(), proposalDTO.getEnterprise().getConstructionName()));
        repository.save(proposal);
        conditionService.insertAll(proposalDTO.getConditions(), proposal.getKey());
        return proposal.getKey();
    }
}
