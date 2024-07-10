package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import org.mapstruct.Mapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IProposalMapper {

	@Named("updateFromProposalDTO")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	@Mapping(target = "enterprise", ignore = true)
	@Mapping(target = "conditions", ignore = true)
	void updateFromProposalDTO(@MappingTarget Proposal proposal, ProposalDTO proposalDTO);
}
