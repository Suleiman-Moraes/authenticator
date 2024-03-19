package com.moraes.authenticator.api.mapper;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import org.mapstruct.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Named("updateFromUserDTOForMe")
	@BeanMapping(unmappedTargetPolicy = ReportingPolicy.IGNORE)
	@Mapping(target = "profile", ignore = true)
	@Mapping(target = "password", ignore = true)
	void updateFromUserDTOForMe(@MappingTarget User user, UserDTO userDTO);
}
