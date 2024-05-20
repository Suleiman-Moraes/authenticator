package com.moraes.authenticator.api.service.interfaces.real_state;

import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.service.interfaces.IConverterService;
import com.moraes.authenticator.api.service.interfaces.IServiceDelete;
import com.moraes.authenticator.api.service.interfaces.IServiceFindAll;

public interface IProposalService
        extends IServiceFindAll<Proposal, Long>, IServiceDelete<Proposal, Long>,
        IConverterService<Proposal, Long> {

    Long insert(ProposalDTO proposalDTO);
}
