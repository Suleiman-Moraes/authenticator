package com.moraes.authenticator.api.controller;

import static com.moraes.authenticator.api.util.ConstantsTestUtil.USER_MESSAGES;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.dto.user.UserNewPasswordDTO;
import com.moraes.authenticator.api.model.dto.user.UserResetPasswordDTO;

@WebMvcTest
class UserMeControllerTest extends AbstractBasicControllerTest {

    private static final String BASE_URL = "/api/v1/user/me";

    private MockSecurity mockSecurity;

    private MockUser input;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        input = new MockUser();
        mockSecurity = new MockSecurity();
        // Mock Auth
        mockSecurity.mockSuperUser();
    }

    @Test
    @DisplayName("JUnit test Given context When update disabled me Then return no content")
    void testGivenContextWhenUpdateDisabledMeThenReturnNoContent() throws Exception {

        // Mock Auth
        mockSecurity.mockSuperUser();

        // Given / Arrange

        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/disabled")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()));

        // Then / Assert
        response.andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("JUnit test Given UserNewPasswordDTO When update password me Then return no content")
    void testGivenUserNewPasswordDTOWhenUpdatePasswordMeThenReturnNoContent() throws Exception {
        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockUserNewPasswordDTO()))
                .with(csrf()));

        response.andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("JUnit test Given invalid UserNewPasswordDTO When update password me Then return no content")
    void testGivenInvalidUserNewPasswordDTOWhenUpdatePasswordMeThenReturnNoContent() throws Exception {
        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserNewPasswordDTO(null, null)))
                .with(csrf()));

        response.andDo(print()).andExpect(status().isBadRequest());
        // Then / Assert
        response.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"userNewPasswordDTO.oldPassword\" deve ser informado.")))
                .andExpect(jsonPath(USER_MESSAGES,
                        hasItem("Campo \"userNewPasswordDTO.newPassword\" deve ser informado.")));
    }

    @Test
    @DisplayName("JUnit test Given UserResetPasswordDTO When resetPassword me Then return no content")
    void testGivenUserResetPasswordDTOWhenResetPasswordMeThenReturnNoContent() throws Exception {
        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input.mockUserResetPasswordDTO()))
                .with(csrf()));

        response.andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("JUnit test Given Invalid UserResetPasswordDTO When resetPassword me Then return bad request")
    void testGivenInvalidUserResetPasswordDTOWhenResetPasswordMeThenReturnBadRequest() throws Exception {
        // When / Act
        ResultActions response = mockMvc.perform(patch(BASE_URL + "/password/reset")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new UserResetPasswordDTO("", "")))
                .with(csrf()));

        response.andDo(print()).andExpect(status().isBadRequest())
                .andExpect(jsonPath(USER_MESSAGES.concat(".size()"), is(4)));
    }
}
