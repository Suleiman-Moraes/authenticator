package com.moraes.authenticator.api.service.real_state;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.controller.ProposalController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalFilterDTO;
import com.moraes.authenticator.api.model.dto.real_state.proposal.ProposalListDTO;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IProposalRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConditionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;
import com.moraes.authenticator.api.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ProposalService implements IProposalService {

    @Getter
    private final IProposalRepository repository;

    private final IEnterpriseService enterpriseService;

    private final IConditionService conditionService;

    @Override
    public Long insert(ProposalDTO proposalDTO) {
        Proposal proposal = Mapper.parseObject(proposalDTO, Proposal.class);
        proposal.getEnterprise().setKey(
                enterpriseService.insert(proposal.getEnterprise(), proposalDTO.getEnterprise().getConstructionName()));
        repository.save(proposal);
        conditionService.insertAll(proposal.getConditions(), proposal.getKey());
        return proposal.getKey();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProposalListDTO> findPageAll(ProposalFilterDTO filter) {
        final Map<String, Class<?>> fields = getMapOfFields();
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("companyKey", SecurityUtil.getCompanyPrincipalOrThrow().getKey());
        Page<ProposalListDTO> page = repository.page(filter, fields, ProposalListDTO.class, Proposal.class,
                "x.enterprise.construction.company.key = :companyKey", parameters);
        page.getContent().forEach(dto -> addLinks(dto, (long) dto.getKey(), ProposalController.class));
        return page;
    }

    @Override
    public Proposal findByKey(Long key) {
        return repository
                .findByIdAndEnterpriseConstructionCompanyKey(key, SecurityUtil.getCompanyPrincipalOrThrow().getKey())
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public ProposalDTO parse(Proposal proposal) {
        ProposalDTO proposalDTO = Mapper.parseObject(proposal, ProposalDTO.class);
        proposalDTO.setEnterprise(
                enterpriseService.parseOtherFields(proposalDTO.getEnterprise(), proposal.getEnterprise()));
        return proposalDTO;
    }

    public Map<String, Class<?>> getMapOfFields() {
        final Map<String, Class<?>> fields = new LinkedHashMap<>();
        fields.put("x.key", Number.class);
        fields.put("x.enterprise.name", String.class);
        fields.put("x.enterprise.unit", String.class);
        fields.put("x.sizeM2", BigDecimal.class);
        fields.put("x.value", BigDecimal.class);
        fields.put("x.date", LocalDate.class);
        return fields;
    }
}
