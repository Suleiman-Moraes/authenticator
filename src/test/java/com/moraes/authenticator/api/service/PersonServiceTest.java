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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.repository.IPersonRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;

class PersonServiceTest {

    private static final String RETURN_NOT_EQUAL = "Return not equal";
    private static final Long KEY = 1L;

    private MockPerson input;

    @Spy
    @InjectMocks
    private PersonService service;

    @Mock
    private IPersonRepository repository;
    @Mock
    private IUserService userService;
    private Person entity;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testFindByKey() {
        when(repository.findById(KEY)).thenReturn(Optional.of(entity));
        Person ret = service.findByKey(KEY);
        assertNotNull(ret, "Return null");
        assertEquals(entity, ret, RETURN_NOT_EQUAL);
    }

    @Test
    void testFindByKeyThrowResourceNotFoundException() {
        when(repository.findById(KEY)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findByKey(KEY), "Does Not Throw");
    }

    @Test
    void testDelete() {
        when(repository.findById(KEY)).thenReturn(Optional.of(entity));
        assertDoesNotThrow(() -> service.delete(KEY), "Does Not Throw");
    }

    @Test
    void testFindAll() {
        final List<Person> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, service.findAll(), RETURN_NOT_EQUAL);
    }

    @Test
    void testInsert() {
        assertEquals(KEY, service.insert(entity), RETURN_NOT_EQUAL);
    }

    @Test
    void testUpdate() {
        Person entity = input.mockEntity(2);
        entity.setKey(KEY);
        when(repository.save(any())).thenReturn(entity);
        when(repository.findById(KEY)).thenReturn(Optional.of(entity));
        PersonDTO dto = input.mockPersonDTO(2);
        assertDoesNotThrow(() -> service.update(dto, KEY), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testGetMe() {
        User user = new MockUser().mockEntity(1);
        when(userService.getMe()).thenReturn(user);
        when(repository.findByUserKey(user.getKey())).thenReturn(Optional.of(entity));
        assertEquals(entity, service.getMe(), RETURN_NOT_EQUAL);
    }

    @Test
    void testFindPageAll() {
        final PersonFilterDTO filter = new PersonFilterDTO();
        final List<PersonListDTO> list = input.mockPersonListDTOList(10);
        final Page<PersonListDTO> page = new PageImpl<>(list);
        when(repository.page(filter, service.getMapOfFields(), PersonListDTO.class, Person.class)).thenReturn(page);

        assertEquals(list,
                service.findPageAll(filter).getContent(), RETURN_NOT_EQUAL);
    }

    @Test
    void testInsertMe() {
        assertEquals(KEY, service.insertMe(entity), RETURN_NOT_EQUAL);
    }
}
