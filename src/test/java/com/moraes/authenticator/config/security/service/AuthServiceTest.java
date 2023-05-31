package com.moraes.authenticator.config.security.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.moraes.authenticator.api.mock.MockUser;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.config.security.JwtTokenProvider;
import com.moraes.authenticator.config.security.dto.AccountCredentialsDTO;
import com.moraes.authenticator.config.security.dto.TokenDTO;

class AuthServiceTest {

    @Spy
    @InjectMocks
    private AuthService service;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private IUserService userService;

    private AccountCredentialsDTO data;

    private TokenDTO token;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        data = AccountCredentialsDTO.builder().username("test").password("123456").build();
        token = TokenDTO.builder().username(data.getUsername()).accessToken("aiosdaoisdhoaisdhoasihd").build();
    }

    @Test
    void testRefreshToken() {
        final User user = new MockUser().mockEntity(1);
        when(userService.loadUserByUsername(data.getUsername())).thenReturn(user);
        when(tokenProvider.createAccessToken(data.getUsername(), user.getRoles())).thenReturn(token);
        final TokenDTO token = service.signin(data);
        assertNotNull(token, "Return null");
        assertEquals("test", token.getUsername(), "Return not equal");
    }

    @Test
    void testSignin() {
        when(tokenProvider.refreshToken(anyString())).thenReturn(token);
        when(userService.existsByUsername(token.getUsername())).thenReturn(true);
        final TokenDTO token = service.refreshToken(anyString());
        assertNotNull(token, "Return null");
    }

    @Test
    void testSigninThrowUsernameNotFoundException() {
        when(tokenProvider.refreshToken(anyString())).thenReturn(token);
        when(userService.existsByUsername(token.getUsername())).thenReturn(false);
        assertThrows(UsernameNotFoundException.class, ()-> service.refreshToken("test"), "Throw Exception");
    }
}
