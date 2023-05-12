package com.moraes.authenticator.api.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.moraes.authenticator.api.model.User;

public interface IUserService extends UserDetailsService, IServiceDelete<User, Long> {

    boolean existsByUsername(String username);

    void update(User user, Long personKey);
}
