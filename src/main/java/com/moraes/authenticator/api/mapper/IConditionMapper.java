package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.real_state.Condition;
import org.mapstruct.Mapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IConditionMapper {

	@Named("updateFromConditionDTO")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	@Mapping(target = "proposal", ignore = true)
	void updateFromConditionDTO(@MappingTarget Condition condition, ConditionDTO conditionDTO);
}
