package com.moraes.authenticator.api.service.interfaces;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;
import com.moraes.authenticator.api.model.dto.user.UserMeDTO;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;

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

    /**
     * This code snippet defines a method called changePasswordMe that updates the
     * password for the logged-in user. It takes in a UserNewPasswordDTO object
     * containing the old and new passwords.
     * 
     * Inside the method, it retrieves the logged-in user from the repository based
     * on their username. It then verifies the old password provided in the dto
     * object against the user's current password. If the verification is
     * successful, it encodes the new password using a passwordEncoder and saves the
     * updated user information.
     *
     * @param dto the UserNewPasswordDTO object containing the old and new passwords
     */
    void changePasswordMe(UserNewPasswordDTO dto);
}
