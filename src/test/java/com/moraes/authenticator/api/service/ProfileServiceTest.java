package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.mock.MockProfile;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.ProfileDTO;
import com.moraes.authenticator.api.repository.IProfileRepository;

class ProfileServiceTest {

    private static final Long COMMON_USER_PROFILE_ID = 3L;

    private MockProfile input;

    @Spy
    @InjectMocks
    private ProfileService service;

    @Mock
    private IProfileRepository repository;

    private final Long key = 1l;
    private Profile entity;

    @BeforeEach
    void setUp() {
        input = new MockProfile();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(key);
    }

    @Test
    void testFindByKey() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        Profile ret = service.findByKey(key);
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

    @Test
    void testDeleteThrowValidException() {
        Profile entity = input.mockEntity(2);
        entity.setUsers(new MockUser().mockEntityList());

        when(repository.findById(key)).thenReturn(Optional.of(entity));
        assertThrows(ValidException.class, () -> service.delete(key), "Does Throw");
    }

    @Test
    void testFindAll() {
        final List<Profile> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll(), "Return not equal");
    }

    @Test
    void testInsert() {
        assertEquals(key, service.insert(entity), "Return not equal");
    }

    @Test
    void testUpdate() {
        Profile entity = input.mockEntity(2);
        entity.setKey(key);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        ProfileDTO dto = input.mockProfileDTO(2);
        assertDoesNotThrow(() -> service.update(dto, key), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testPreInsertMe() {
        assertEquals(COMMON_USER_PROFILE_ID, service.preInsertMe(new Profile()).getKey(), "Return not equal");
    }
}
