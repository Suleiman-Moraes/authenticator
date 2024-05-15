package com.moraes.authenticator.api.service.interfaces.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;

public interface IConditionService {

    void insertAll(List<ConditionDTO> dtos, Long proposalKey);
}
