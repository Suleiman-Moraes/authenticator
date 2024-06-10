package com.moraes.authenticator.api.service.interfaces.real_state;

import org.springframework.data.domain.Page;

import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.service.interfaces.IConverterService;
import com.moraes.authenticator.api.service.interfaces.IServiceDelete;
import com.moraes.authenticator.api.service.interfaces.IServiceFindByKey;

@SuppressWarnings("unused")
public interface IProposalService
        extends IServiceDelete<Proposal, Long>, IConverterService<Proposal, Long>, IServiceFindByKey<Proposal, Long> {

    Long insert(ProposalDTO proposalDTO);

    Page<ProposalListDTO> findPageAll(ProposalFilterDTO filter);

    ProposalDTO parse(Proposal proposal);
}
