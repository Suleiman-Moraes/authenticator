package com.moraes.authenticator.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.menu.MockMenu;
import com.moraes.authenticator.api.model.dto.menu.MenuDTO;

@WebMvcTest
class MenuControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/menu";

    private MockMenu input;

    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        input = new MockMenu();
        mockSecurity = new MockSecurity();
    }

    @Test
    @DisplayName("Junit Test Given Context When FindAll Then Return List")
    void givenContextWhenFindAllThenReturnList() throws Exception {
        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange
        final List<MenuDTO> dtos = input.mockMenuDTOList();
        given(menuService.findAll()).willReturn(dtos);

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL));

        // Then / Assert
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(14)));
    }
}
