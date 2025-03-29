package com.moraes.authenticator.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;
import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.service.interfaces.IPersonService;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuService;
import com.moraes.authenticator.api.service.interfaces.menu.IQuestionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;
import com.moraes.authenticator.api.service.interfaces.real_state.IProposalService;
import com.moraes.authenticator.config.security.JwtTokenProvider;
import com.moraes.authenticator.config.security.interfaces.IAuthService;

public abstract class AbstractBasicControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected IAuxiliaryListService auxiliaryListService;

    @MockitoBean
    protected IPersonService personService;

    @MockitoBean
    protected IBasicTokenService basicTokenService;

    @MockitoBean
    protected IProfileService profileService;

    @MockitoBean
    protected IUserService userService;

    @MockitoBean
    protected IAuthService authService;

    @MockitoBean
    protected IMenuService menuService;

    @MockitoBean
    protected IQuestionService questionService;

    @MockitoBean
    protected JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    protected IProposalService proposalService;

    @MockitoBean
    protected IConstructionService constructionService;

    @MockitoBean
    protected IEnterpriseService enterpriseService;
}
