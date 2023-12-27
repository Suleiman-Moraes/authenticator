package com.moraes.authenticator.api.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import com.moraes.authenticator.api.mock.MockSecurity;

@WebMvcTest
public class UserMeControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/user/me";

    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        mockSecurity = new MockSecurity();
        // Mock Auth
        mockSecurity.mockSuperUser();
    }

    // ResponseEntity<Void> updateEnabledMe
    @Test
    @DisplayName("JUnit test Given context When update enabled me Then return no content")
    void testGivenContextWhenUpdateEnabledMeThenReturnNoContent() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/enabled")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isNoContent());
    }
}
