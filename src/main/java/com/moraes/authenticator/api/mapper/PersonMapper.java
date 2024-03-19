package com.moraes.authenticator.api.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PersonMapper {

	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	@Mapping(target = "user", source = "user", qualifiedByName = "updateFromUserDTOForMe")
	void updateFromPersonDTO(@MappingTarget Person person, PersonDTO personDTO);

	// @BeanMapping(ignoreByDefault = true, nullValuePropertyMappingStrategy =
	// NullValuePropertyMappingStrategy.IGNORE)
	// @Mapping(target = "user", source = "user", qualifiedByName =
	// "updateFromUserDTOForMe")
	// void updateFromPersonDTO(@MappingTarget Person person, PersonDTO personDTO);
}
