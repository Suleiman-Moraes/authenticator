package com.moraes.authenticator.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;

class PersonMapperTest {

    @Spy
    @InjectMocks
    private PersonMapper mapper = Mappers.getMapper(PersonMapper.class);

    @Mock
    private UserMapper userMapper;

    private MockPerson input;

    private static final Long KEY = 1L;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        input = new MockPerson();
    }

    @Test
    @DisplayName("JUnit test given person and personDTO when updateFromPersonDTO then update person")
    void testGivenPersonAndPersonDTOWhenUpdateFromPersonDTOThenUpdatePerson() {
        Person person = input.mockEntity(1);
        person.setKey(KEY);
        PersonDTO personDTO = input.mockPersonDTO(2);

        mapper.updateFromPersonDTO(person, personDTO);

        assertNotNull(person.getUser(), "User is null");
        assertEquals(personDTO.getName(), person.getName(), "Name is different");
        assertEquals(personDTO.getEmail(), person.getEmail(), "Email is different");
        assertEquals(personDTO.getAddress(), person.getAddress(), "Address is different");
        assertEquals(KEY, person.getKey(), "Key is different");
        verify(userMapper, times(1)).updateFromUserDTOForMe(any(), any());
    }

    @Test
    @DisplayName("JUnit test given person and personDTO with null values when updateFromPersonDTO then update person")
    void testGivenPersonAndPersonDTOWithNullValuesWhenUpdateFromPersonDTOThenUpdatePerson() {
        Person person = input.mockEntity(1);
        PersonDTO personDTO = new PersonDTO();

        mapper.updateFromPersonDTO(person, personDTO);

        assertNull(person.getUser(), "User is not null");
        assertNull(person.getName(), "Name is not null");
        assertNull(person.getEmail(), "Email is not null");
        assertNull(person.getAddress(), "Address is not null");
        verify(userMapper, times(0)).updateFromUserDTOForMe(any(), any());
    }
}
