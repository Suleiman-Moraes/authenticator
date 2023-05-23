package com.moraes.authenticator.api.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.UserDTO;

public interface IUserService extends UserDetailsService, IServiceDelete<User, Long> {

    boolean existsByUsername(String username);

    void update(UserDTO object, Long key);

    Long insert(User entity, Long personKey);

    User getMe();

    void validInsert(User entity);
}
