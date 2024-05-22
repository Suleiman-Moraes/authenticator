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
import com.moraes.authenticator.api.mock.real_state.MockEnterprise;
import com.moraes.authenticator.api.model.real_state.Enterprise;

@WebMvcTest
class EnterpriseControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/enterprise";

    private MockEnterprise input;

    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        input = new MockEnterprise();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("JUnit test Given constructionName When getNameByConstructionName Then return List of names of enterprises")
    void testGivenConstructionNameWhenGetNameByConstructionNameThenReturnListOfNamesOfEnterprises() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final String constructionName = "Test";
        given(enterpriseService.getNameByConstructionName(constructionName))
                .willReturn(input.mockNameList(input.getMaxSize()));

        // When / Act
        ResultActions response = mockMvc
                .perform(get(BASE_URL.concat("/names")).queryParam("constructionName", constructionName));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(input.getMaxSize())));
    }

    @Test
    @DisplayName("JUnit test Given null constructionName When getNameByConstructionName Then return BadRequest")
    void testGivenNullConstructionNameWhenGetNameByConstructionNameThenReturnBadRequest() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // When / Act
        ResultActions response = mockMvc
                .perform(get(BASE_URL.concat("/names")));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("JUnit test Given name and constructionName When getByNameAndConstructionName Then return EnterpriseDTO")
    void testGivenNameAndConstructionNameWhenGetByNameAndConstructionNameThenReturnEnterpriseDTO() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final String name = "Test";
        final String constructionName = "Test2";
        Enterprise entity = input.mockEntity(1);
        entity.setName(name);
        entity.getConstruction().setName(constructionName);
        given(enterpriseService.getByNameAndConstructionName(name, constructionName))
                .willReturn(entity);

        // When / Act
        ResultActions response = mockMvc
                .perform(get(BASE_URL.concat("/name")).queryParam("name", name).queryParam("constructionName",
                        constructionName));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.constructionName", is(constructionName)))
                .andExpect(jsonPath("$.value", is(entity.getValue().doubleValue())))
                .andExpect(jsonPath("$.vpl", is(entity.getVpl().doubleValue())))
                .andExpect(jsonPath("$.valueM2", is(entity.getValueM2().doubleValue())))
                .andExpect(jsonPath("$.sizeM2", is(entity.getSizeM2().doubleValue())))
                .andExpect(jsonPath("$.unit", is(entity.getUnit())));
    }

    @Test
    @DisplayName("JUnit test Given null name and null constructionName When getByNameAndConstructionName Then return BadRequest")
    void testGivenNullNameAndNullConstructionNameWhenGetByNameAndConstructionNameThenReturnBadRequest()
            throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // When / Act
        ResultActions response = mockMvc
                .perform(get(BASE_URL.concat("/name")));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest());
    }
}
