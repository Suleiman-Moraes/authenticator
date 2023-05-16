package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.PersonDTO;
import com.moraes.authenticator.api.repository.IPersonRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;

class PersonServiceTest {

    private MockPerson input;

    @Spy
    @InjectMocks
    private PersonService service;

    @Mock
    private IPersonRepository repository;
    @Mock
    private IUserService userService;

    private final Long key = 1l;
    private Person entity;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(key);
    }

    @Test
    void testFindByKey() {
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        Person ret = service.findByKey(key);
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
    void testFindAll() {
        final List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll(), "Return not equal");
    }

    @Test
    void testInsert() {
        assertEquals(key, service.insert(entity), "Return not equal");
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(2);
        entity.setKey(key);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(key)).thenReturn(Optional.of(entity));
        PersonDTO dto = input.mockPersonDTO(2);
        assertDoesNotThrow(() -> service.update(dto, key), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testParseObjectForUpdate() {
        PersonDTO dto = input.mockPersonDTO(1);
        assertNotNull(dto.getUser(), "Return not equal");
        assertNotNull(dto.getUser().getUsername(), "Return not equal");
        assertNull(service.parseObjectForUpdate(dto).getUser(), "Return not equal");
    }
}
