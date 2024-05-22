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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.real_state.MockConstruction;

@WebMvcTest
class ConstructionControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/construction";

    private MockConstruction input;

    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        input = new MockConstruction();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("Junit test given context when getNameAll then return list of name of construction")
    void givenContextWhenGetNameAllThenReturnListOfNameOfConstruction() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        given(constructionService.getNameAll()).willReturn(input.mockNameList(input.getMaxSize()));

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL.concat("/name")));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(input.getMaxSize())));
    }
}
