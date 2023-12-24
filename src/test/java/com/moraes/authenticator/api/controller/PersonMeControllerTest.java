package com.moraes.authenticator.api.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;

@WebMvcTest
class PersonMeControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/person/me";

    private MockPerson input;

    private MockSecurity mockSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        input = new MockPerson();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("JUnit test Given context When get me Then return PersonDTO")
    void testGivenContextWhenGetMeThenReturnPersonDTO() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final Person person = input.mockEntity(1);
        given(personService.getMe()).willReturn(person);

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(person.getName())))
                .andExpect(jsonPath("$.address", is(person.getAddress())));

    }

    @Test
    @DisplayName("JUnit test Given PersonMeDTO When update me Then return PersonDTO")
    void testGivenPersonMeDTOWhenUpdateMeThenReturnPersonDTO() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonMeDTO person = input.mockPersonMeDTO(1);
        given(personService.updateMe(person)).willReturn(1L);

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    @DisplayName("JUnit test Given invalid PersonMeDTO When update me Then return PersonDTO")
    void testGivenInvalidPersonMeDTOWhenUpdateMeThenReturnPersonDTO() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonMeDTO person = new PersonMeDTO();
        given(personService.updateMe(person)).willReturn(1L);

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userMessages", hasItem("Campo \"personMeDTO.user\" deve ser informado.")))
                .andExpect(jsonPath("$.userMessages", hasItem("Campo \"personMeDTO.name\" deve ser informado.")));
    }

    @Test
    @DisplayName("JUnit test Given PersonMeDTO When insert me Then return Long")
    void testGivenPersonMeDTOWhenInsertMeThenReturnLong() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonMeDTO person = input.mockPersonMeDTO(1);
        given(personService.insertMe(any(Person.class))).willReturn(1L);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(1)));
    }

    @Test
    @DisplayName("JUnit test Given Invalid PersonMeDTO When insert me Then return Long")
    void testGivenInvalidPersonMeDTOWhenInsertMeThenReturnLong() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonMeDTO person = new PersonMeDTO();

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.userMessages", hasItem("Campo \"personMeDTO.name\" deve ser informado.")))
                .andExpect(jsonPath("$.userMessages", hasItem("Campo \"personMeDTO.user\" deve ser informado.")));
    }

}
