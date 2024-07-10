package com.moraes.authenticator.api.service.interfaces.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.real_state.Condition;

public interface IConditionService {

    void insertAll(List<Condition> conditions, Long proposalKey);

	void updateAll(List<Condition> conditions, List<ConditionDTO> conditionDTOs, Long proposalKey);
}
