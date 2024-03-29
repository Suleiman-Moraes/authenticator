package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        mockGetMe();
        when(repository.findByKeyAndUserCompanyKey(eq(KEY), any())).thenReturn(Optional.of(entity));
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
        mockGetMe();
        when(repository.save(any())).thenReturn(entity);
        when(repository.findByKeyAndUserCompanyKey(eq(KEY), any())).thenReturn(Optional.of(entity));
        PersonDTO dto = input.mockPersonDTO(2);
        assertDoesNotThrow(() -> service.update(dto, KEY), "Does Not Throw");
        assertNotNull(entity, "Return null");
        assertNotEquals(this.entity, entity, "Return equal");
    }

    @Test
    void testGetMe() {
        mockGetMe();
        assertEquals(entity, service.getMe(), RETURN_NOT_EQUAL);
    }

    @Test
    void testFindPageAll() {
        final int maxSize = 10;
        final PersonFilterDTO filter = new PersonFilterDTO();
        final List<PersonListDTO> list = input.mockPersonListDTOListWithKey(maxSize);
        final Page<PersonListDTO> page = new PageImpl<>(list);
        mockGetMe();
        when(repository.page(eq(filter), any(), eq(PersonListDTO.class), eq(Person.class), anyString(), any()))
                .thenReturn(page);

        final Page<PersonListDTO> pages = service.findPageAll(filter);
        assertNotNull(pages, "Return null");
        for (int index = 1; index <= maxSize; index++) {
            final var dto = pages.getContent().get(index - 1);
            final var entity = list.get(index - 1);
            assertEquals(dto.getLinks().toList().get(0).getHref(), "/api/v1/person/" + index,
                    "Href not equal");
            assertEquals(dto.getName(), entity.getName(), "Name not equal");
            assertEquals(dto.getKey(), entity.getKey(), "Key not equal");
            assertEquals(dto.getAddress(), entity.getAddress(), "Address not equal");
            assertEquals(dto.getUsername(), entity.getUsername(), "Username not equal");
            assertEquals(dto.getProfileDescription(), entity.getProfileDescription(),
                    "ProfileDescription not equal");
        }
    }

    @Test
    void testInsertMe() {
        assertEquals(KEY, service.insertMe(entity), RETURN_NOT_EQUAL);
    }

    @Test
    @DisplayName("JUnit test Given key and company key from context When findByKeyAndCompanyKey Then return entity")
    void testGivenKeyAndCompanyKeyFromContextWhenFindByKeyAndCompanyKeyThenReturnEntity() {
        mockGetMe();
        when(repository.findByKeyAndUserCompanyKey(KEY, null)).thenReturn(Optional.of(entity));
        assertEquals(entity, service.findByKeyAndCompanyKey(KEY), RETURN_NOT_EQUAL);
    }

    private void mockGetMe() {
        User user = new MockUser().mockEntity(1);
        when(userService.getMe()).thenReturn(user);
        when(repository.findByUserKey(user.getKey())).thenReturn(Optional.of(entity));
    }
}
