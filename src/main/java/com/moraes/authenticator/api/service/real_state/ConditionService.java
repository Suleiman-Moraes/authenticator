package com.moraes.authenticator.api.service.real_state;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IConditionRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConditionService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ConditionService implements IConditionService {

    @Getter
    private IConditionRepository repository;

    // TODO - Alterar para receber o model direto, ao invés de DTO
    @Override
    public void insertAll(List<ConditionDTO> dtos, Long proposalKey) {
        if (!CollectionUtils.isEmpty(dtos)) {
            dtos.forEach(dto -> Optional.ofNullable(dto).ifPresent(d -> insert(d, proposalKey)));
        }
    }

    public void insert(ConditionDTO dto, Long proposalKey) {
        Condition condition = Mapper.parseObject(dto, Condition.class);
        condition.setProposal(Proposal.builder().key(proposalKey).build());
        save(condition);
    }

    public Long save(Condition object) {
        object.setAmount(object.getValueInstallments().multiply(BigDecimal.valueOf(object.getNumberInstallments())));
        return repository.save(object).getKey();
    }
}
