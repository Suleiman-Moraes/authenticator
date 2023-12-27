package com.moraes.authenticator.api.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import com.moraes.authenticator.api.model.dto.user.UserEnabledDTO;

@WebMvcTest
public class UserControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/user";

    private MockSecurity mockSecurity;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockSecurity = new MockSecurity();
        // Mock Auth
        mockSecurity.mockSuperUser();
    }

    @Test
    @DisplayName("JUnit test Given user key and UserEnabledDTO When update enabled Then return no content")
    void testGivenUserKeyAndUserEnabledDTOWhenUpdateEnabledThenReturnNoContent() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/enabled/{key}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserEnabledDTO(true)))
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("JUnit test Given user key and Invalid UserEnabledDTO When update enabled Then return no content")
    void testGivenUserKeyAndInvalidUserEnabledDTOWhenUpdateEnabledThenReturnNoContent() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/enabled/{key}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isBadRequest());
    }
}
