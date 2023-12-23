package com.moraes.authenticator.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;
import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.service.interfaces.IPersonService;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.config.security.JwtTokenProvider;
import com.moraes.authenticator.config.security.interfaces.IAuthService;

public abstract class AbstractBasicControllerTest {
    
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected IAuxiliaryListService auxiliaryListService;

    @MockBean
    protected IPersonService personService;

    @MockBean
    protected IBasicTokenService basicTokenService;

    @MockBean
    protected IProfileService profileService;

    @MockBean
    protected IUserService userService;

    @MockBean
    protected IAuthService authService;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
}
