package com.moraes.authenticator.api.service.real_state;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    @Override
    public void insertAll(List<Condition> conditions, Long proposalKey) {
        if (!CollectionUtils.isEmpty(conditions)) {
            conditions.forEach(condition -> Optional.ofNullable(condition).ifPresent(d -> insert(d, proposalKey)));
        }
    }

    public void insert(Condition condition, Long proposalKey) {
        condition.setProposal(Proposal.builder().key(proposalKey).build());
        save(condition);
    }

    public Long save(Condition object) {
        object.setAmount(object.getValueInstallments().multiply(BigDecimal.valueOf(object.getNumberInstallments())));
        return repository.save(object).getKey();
    }
}
