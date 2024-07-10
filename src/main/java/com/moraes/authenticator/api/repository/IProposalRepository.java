package com.moraes.authenticator.api.repository;

import java.util.Optional;

import com.moraes.authenticator.api.model.real_state.Proposal;

public interface IProposalRepository extends IExtendedRepository<Proposal, Long> {

    Optional<Proposal> findByKeyAndEnterpriseConstructionCompanyKey(Long key, Long companyKey);
}
