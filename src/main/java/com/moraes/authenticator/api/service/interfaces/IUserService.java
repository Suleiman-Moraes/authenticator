package com.moraes.authenticator.api.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.api.model.dto.user.UserMeDTO;

public interface IUserService extends UserDetailsService, IServiceDelete<User, Long> {

    boolean existsByUsername(String username);

    void update(UserDTO object, Long key);

    void updateMe(UserMeDTO object, Long key);

    User preInsertMe(User user);

    Long insert(User entity, Long personKey);

    User getMe();

    void validInsert(User entity);

    User updateEnabled(UserEnabledDTO entity, long key);

    User updateDisabledMe();
}
