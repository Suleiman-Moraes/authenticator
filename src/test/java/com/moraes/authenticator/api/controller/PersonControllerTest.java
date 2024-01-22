package com.moraes.authenticator.api.controller;

import static com.moraes.authenticator.api.util.ConstantsTestUtil.USER_MESSAGES;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moraes.authenticator.api.mock.MockPerson;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;

@WebMvcTest
class PersonControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/person";

    private static final String BASE_URL_KEY = BASE_URL + "/{key}";

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
    @DisplayName("JUnit test Given Person key When find by key Then return PersonDTO")
    void testGivenPersonKeyWhenFindByKeyThenReturnPersonDTO() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final Person person = input.mockEntity(1);

        given(personService.findByKeyAndCompanyKey(1L)).willReturn(person);
        given(personService.parseObject(person, PersonDTO.class, PersonController.class))
                .willReturn(input.mockPersonDTO(1));

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL_KEY, 1L));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(person.getName())))
                .andExpect(jsonPath("$.address", is(person.getAddress())));
    }

    @Test
    @DisplayName("JUnit test Given Context When findAll Then return Page of PersonListDTO")
    void testGivenContextWhenFindAllThenReturnPageOfPersonListDTO() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PageRequest pageRequest = PageRequest.of(0, 10);
        given(personService.findPageAll(any()))
                .willReturn(new PageImpl<>(input.mockPersonListDTOList(10), pageRequest,
                        input.getMaxSize()));

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(input.getMaxSize())))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.content.size()", is(10)));
    }

    @Test
    @DisplayName("JUnit test Given PersonDTO When insert Then return Long key")
    void testGivenPersonDTOWhenInsertThenReturnLongKey() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonDTO person = input.mockPersonDTO(1);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("JUnit test Given Invalid PersonDTO With null values When insert Then return BadRequest")
    void testGivenInvalidPersonDTOWithNullValuesWhenInsertThenReturnBadRequest() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonDTO person = new PersonDTO();

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.user\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.email\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.name\" deve ser informado.")));
    }

    @Test
    @DisplayName("JUnit test Given Invalid PersonDTO With wrong values When insert Then return BadRequest")
    void testGivenInvalidPersonDTOWithWrongValuesWhenInsertThenReturnBadRequest() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final PersonDTO person = input.mockPersonWrongValuesDTOWrongValues();

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(person))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"personDTO.address\" deve estar entre 0 e 255.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"personDTO.name\" deve estar entre 2 e 150.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.email\" é um e-mail inválido.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"personDTO.email\" deve estar entre 5 e 150.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"personDTO.user.password\" deve estar entre 6 e 30.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"personDTO.user.username\" deve estar entre 2 e 150.")));
    }

    @Test
    @DisplayName("JUnit test Given PersonDTO When update Then return Long key")
    void testGivenPersonDTOWhenUpdateThenReturnLongKey() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL_KEY, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockPersonDTO(1)))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test Given Invalid PersonDTO When update Then return BadRequest")
    void testGivenInvalidPersonDTOWhenUpdateThenReturnBadRequest() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL_KEY, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new PersonDTO()))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.user\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"personDTO.name\" deve ser informado.")));
    }

    @Test
    @DisplayName("JUnit test Given Person key When delete Then return Void")
    void testGivenPersonKeyWhenDeleteThenReturnVoid() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(delete(BASE_URL_KEY, 1L)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isNoContent());
    }
}
