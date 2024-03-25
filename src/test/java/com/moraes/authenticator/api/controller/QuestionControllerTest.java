package com.moraes.authenticator.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
}
