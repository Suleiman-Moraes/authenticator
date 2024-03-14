package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface PersonMapper {

	@Mapping(target = "user", source = "user", qualifiedByName = "updateFromUserDTOForMe")
	void updateFromPersonDTO(@MappingTarget Person person, PersonDTO personDTO);
}
