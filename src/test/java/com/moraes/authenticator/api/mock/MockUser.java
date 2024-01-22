package com.moraes.authenticator.api.mock;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.KeyDTO;
import com.moraes.authenticator.api.model.dto.user.UserDTO;
import com.moraes.authenticator.api.model.dto.user.UserMeDTO;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordTokenDTO;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockUser extends AbstractMock<User> {

    @Override
    protected Class<User> getClazz() {
        return User.class;
    }

    @Override
    protected void setOdersValues(User entity, Integer number) {
        entity.setProfile(new MockProfile().mockEntity(number));
        entity.setCompany(new MockCompany().mockEntity(number));
    }

    public UserDTO mockUserDTO(Integer number) {
        try {
            UserDTO entity = new UserDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setProfile(new KeyDTO(number.longValue()));
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public UserDTO mockUserDTOWrongValues() {
        try {
            return UserDTO.builder()
                    .username("a")
                    .password("a")
                    .profile(new KeyDTO(1L))
                    .build();
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<UserDTO> mockUserDTOList() {
        List<UserDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockUserDTO(i));
        }
        return entitys;
    }

    public UserMeDTO mockUserMeDTO(Integer number) {
        try {
            UserMeDTO entity = new UserMeDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<UserMeDTO> mockUserMeDTOList() {
        List<UserMeDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockUserMeDTO(i));
        }
        return entitys;
    }

    public UserNewPasswordDTO mockUserNewPasswordDTO() {
        return new UserNewPasswordDTO("oldPassword", "newPassword");
    }

    public UserResetPasswordDTO mockUserResetPasswordDTO() {
        return new UserResetPasswordDTO("username", "email@email.com");
    }

    public UserResetPasswordTokenDTO mockUserResetPasswordTokenDTO() {
        final UUID token = UUID.randomUUID();
        return new UserResetPasswordTokenDTO(token.toString().substring(0, 20), token);
    }
}
