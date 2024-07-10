package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import org.mapstruct.Mapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface IEnterpriseMapper {

	@Named("updateFromEnterpriseDTO")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	@Mapping(target = "construction", ignore = true)
	@Mapping(target = "proposals", ignore = true)
	void updateFromEnterpriseDTO(@MappingTarget Enterprise enterprise, EnterpriseDTO enterpriseDTO);
}
