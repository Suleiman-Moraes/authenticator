package com.moraes.authenticator.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.user.UserDTO;

public class UserMapperTest {

    @Spy
    @InjectMocks
    private UserMapper mapper = Mappers.getMapper(UserMapper.class);

    private MockUser input;

    private static final Long KEY = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        input = new MockUser();
    }

    @Test
    @DisplayName("JUnit test given user and userDTO when updateFromUserDTOForMe then update user")
    void testGivenUserAndUserDTOWhenUpdateFromUserDTOForMeThenUpdateUser() {
        final String password = "password";
        User entity = input.mockEntity(1);
        entity.setKey(KEY);
        entity.setPassword(password);

        UserDTO dto = input.mockUserDTO(2);

        mapper.updateFromUserDTOForMe(entity, dto);

        assertEquals(dto.getUsername(), entity.getUsername(), "Username not equal");

        assertEquals(KEY, entity.getKey(), "Key not equal");
        assertEquals(password, entity.getPassword(), "Password not equal");
        assertNotNull(entity.getProfile(), "Profile is null");
    }

    @Test
    @DisplayName("JUnit test given user and userDTO with null values when updateFromUserDTOForMe then update user")
    void testGivenUserAndUserDTOWithNullValuesWhenUpdateFromUserDTOForMeThenUpdateUser() {
        final String password = "password";
        User entity = input.mockEntity(1);
        entity.setPassword(password);

        UserDTO dto = new UserDTO();

        mapper.updateFromUserDTOForMe(entity, dto);

        assertNull(entity.getUsername(), "Username not null");

        assertEquals(password, entity.getPassword(), "Password not equal");
        assertNotNull(entity.getProfile(), "Profile is null");
    }
}
