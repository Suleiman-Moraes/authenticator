package com.moraes.authenticator.api.service.real_state;

import com.moraes.authenticator.api.mapper.IConditionMapper;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.enums.OperationModifyEnum;
import com.moraes.authenticator.api.model.real_state.Condition;
import com.moraes.authenticator.api.model.real_state.Proposal;
import com.moraes.authenticator.api.repository.IConditionRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConditionService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ConditionService implements IConditionService {

	@Getter
	private final IConditionRepository repository;

	private final IConditionMapper conditionMapper;

	@Override
	public void insertAll(List<Condition> conditions, Long proposalKey) {
		if (!CollectionUtils.isEmpty(conditions)) {
			conditions.forEach(condition -> Optional.ofNullable(condition).ifPresent(d -> insert(d, proposalKey)));
		}
	}

	@Override
	public void updateAll(List<Condition> conditions, List<ConditionDTO> conditionDTOs, Long proposalKey) {
		Map<OperationModifyEnum, List<Condition>> map = carryOutTheSeparation(conditions, conditionDTOs);

		if (!CollectionUtils.isEmpty(map.get(OperationModifyEnum.INSERT))) {
			insertAll(map.get(OperationModifyEnum.INSERT), proposalKey);
		}
		if (!CollectionUtils.isEmpty(map.get(OperationModifyEnum.UPDATE))) {
			updateAll(map.get(OperationModifyEnum.UPDATE));
		}
		if (!CollectionUtils.isEmpty(map.get(OperationModifyEnum.DELETE))) {
			deleteAll(map.get(OperationModifyEnum.DELETE));
		}
	}

	public void deleteAll(@NotNull List<Condition> conditions) {
		conditions.forEach(condition -> repository.deleteById(condition.getKey()));
	}

	public void updateAll(@NotNull List<Condition> conditions) {
		conditions.forEach(this::save);
	}

	public Map<OperationModifyEnum, List<Condition>> carryOutTheSeparation(@NotNull List<Condition> conditions,
			@NotNull List<ConditionDTO> conditionDTOs) {
		final int conditionSize = conditions.size();
		Map<OperationModifyEnum, List<Condition>> map = Map.of(OperationModifyEnum.INSERT, new LinkedList<>(),
				OperationModifyEnum.UPDATE, new LinkedList<>(), OperationModifyEnum.DELETE, new LinkedList<>());
		for (int i = 0; i < conditionDTOs.size(); i++) {
			if (i < conditionSize) {
				Condition condition = conditions.get(i);
				conditionMapper.updateFromConditionDTO(condition, conditionDTOs.get(i));
				map.get(OperationModifyEnum.UPDATE).add(condition);
			}
			else {
				map.get(OperationModifyEnum.INSERT).add(Mapper.parseObject(conditionDTOs.get(i), Condition.class));
			}
		}
		for (int i = conditionDTOs.size(); i < conditionSize; i++) {
			map.get(OperationModifyEnum.DELETE).add(conditions.get(i));
		}

		return map;
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
