package com.moraes.authenticator.api.controller;

import static com.moraes.authenticator.api.util.ConstantsTestUtil.USER_MESSAGES;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
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
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.menu.MockQuestion;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.menu.Question;

@WebMvcTest
public class QuestionControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/question";

    private static final String BASE_URL_KEY = BASE_URL + "/{key}";

    private MockQuestion input;

    private MockSecurity mockSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        input = new MockQuestion();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("JUnit test Given Question key When find by key Then return QuestionDTO")
    void testGivenQuestionKeyWhenFindByKeyThenReturnQuestionDTO() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final Question entity = input.mockEntity(1);

        given(questionService.findByKeyAndCompanyKey(1L)).willReturn(entity);
        given(questionService.parseObject(entity, QuestionDTO.class, QuestionController.class))
                .willReturn(input.mockQuestionDTO(1));

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL_KEY, 1L));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value", is(entity.getValue())))
                .andExpect(jsonPath("$.mask", is(entity.getMask())))
                .andExpect(jsonPath("$.order", is(entity.getOrder())))
                .andExpect(jsonPath("$.typeFrom", is(entity.getTypeFrom().name())))
                .andExpect(jsonPath("$.type", is(entity.getType().name())))
                .andExpect(jsonPath("$.enabled", is(entity.isEnabled())))
                .andExpect(jsonPath("$.required", is(entity.isRequired())));
    }

    @Test
    @DisplayName("JUnit test Given QuestionDTO When insert Then return Long")
    void testGivenQuestionDTOWhenInsertThenReturnLong() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final Long key = 1L;
        given(questionService.insert(any())).willReturn(key);

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockQuestionDTO(1)))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", String.format("%s/%s", BASE_URL, key)))
                .andExpect(jsonPath("$", is(key.intValue())));
    }

    @Test
    @DisplayName("JUnit test Given Invalid QuestionDTO with null values When insert Then return BadRequest")
    void testGivenInvalidQuestionDTOWithNullValuesWhenInsertThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new QuestionDTO()))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".length()")).value(3))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"questionDTO.value\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"questionDTO.type\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"questionDTO.typeFrom\" deve ser informado.")));
    }

    @Test
    @DisplayName("JUnit test Given Invalid QuestionDTO with wrong values When insert Then return BadRequest")
    void testGivenInvalidQuestionDTOWithWrongValuesWhenInsertThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // When / Act
        ResultActions response = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockQuestionDTOWrongValues()))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".length()")).value(3))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"questionDTO.value\" deve estar entre 3 e 255.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("O tamanho do campo \"questionDTO.mask\" deve estar entre 1 e 50.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Valor mínimo do campo \"questionDTO.order\", deve ser 1.")));
    }

    @Test
    @DisplayName("JUnit test Given QuestionDTO and key When update Then return Long")
    void testGivenQuestionDTOWhenUpdateThenReturnLong() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL_KEY, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockQuestionDTO(1)))
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("JUnit test Given QuestionDTO and key When update Then return BadRequest")
    void testGivenQuestionDTOWhenUpdateThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(put(BASE_URL_KEY, 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
                .with(csrf()));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".length()")).value(3));
    }
}
