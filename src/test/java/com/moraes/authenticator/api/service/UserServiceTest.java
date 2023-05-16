package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.UserDTO;
import com.moraes.authenticator.api.repository.IUserRepository;

class UserServiceTest {

    private MockUser input;

    @Spy
    @InjectMocks
    private UserService service;

    @Mock
    private IUserRepository repository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        input = new MockUser();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(key);
    }

    private final Long key = 1l;
    private User entity;

    @Test
    void testExistsByUsername() {
        assertFalse(service.existsByUsername("Test"), "Return not equal");
    }

    @Test
    void testLoadUserByUsername() {
        when(repository.findByUsername(entity.getUsername())).thenReturn(Optional.of(entity));
        User ret = (User) service.loadUserByUsername(entity.getUsername());
        assertNotNull(ret, "Retunr null");
        assertEquals(entity, ret, "Return not equal");
    }

    @Test
    void testLoadUserByUsernameThrowUsernameNotFoundException() {
        final String username = "Test";
        when(repository.findByUsername(username)).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername(username), "Does Not Throw");
    }

    @Test
    void testUpdate() {
        User entity = input.mockEntity(2);
        entity.setKey(key);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        UserDTO dto = input.mockUserDTO(2);
        assertDoesNotThrow(() -> service.update(dto, key), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testInsert() {
        assertEquals(key, service.insert(entity, key), "Return not equal");
    }

    @Test
    void testFindByKey() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        User ret = service.findByKey(key);
        assertNotNull(ret, "Retunr null");
        assertEquals(entity, ret, "Return not equal");
    }

    @Test
    void testFindByKeyThrowResourceNotFoundException() {
        when(repository.findById(key)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByKey(key), "Does Not Throw");
    }

    @Test
    void testDelete() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        assertDoesNotThrow(() -> service.delete(key), "Does Not Throw");
    }
}
