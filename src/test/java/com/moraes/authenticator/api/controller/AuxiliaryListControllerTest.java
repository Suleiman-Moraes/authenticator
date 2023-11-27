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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.moraes.authenticator.api.mock.MockKeyDescriptionDTO;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;
import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.service.interfaces.IPersonService;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.config.security.JwtTokenProvider;
import com.moraes.authenticator.config.security.interfaces.IAuthService;

@WebMvcTest
class AuxiliaryListControllerTest {

    private static final String BASE_URL = "/api/v1/auxiliary-list";

    private MockKeyDescriptionDTO input;

    private MockSecurity mockSecurity;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAuxiliaryListService service;

    @MockBean
    private IPersonService personService;

    @MockBean
    private IBasicTokenService basicTokenService;

    @MockBean
    private IProfileService profileService;

    @MockBean
    private IUserService userService;

    @MockBean
    private IAuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        input = new MockKeyDescriptionDTO();
        mockSecurity = new MockSecurity();
    }

    // ResponseEntity<List<KeyDescriptionDTO<String>>> getListRoleEnum()
    @Test
    @DisplayName("JUnit test Given context When get list role enum Then return list")
    void testGivenContextWhenGetListRoleEnumThenReturnList() throws Exception {
        // Given / Arrange
        // Mock Auth
        mockSecurity.mockSuperUser();
        given(service.getEnumList(RoleEnum.class)).willReturn(input.mockStringList());

        // When / Act
        ResultActions response = mockMvc.perform(get(BASE_URL + "/role-enum")
                .header(ConstantsUtil.AUTHORIZATION, String.format("%s Token", ConstantsUtil.BEARER)));

        // Then / Assert
        response.andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", is(14)));
    }
}
