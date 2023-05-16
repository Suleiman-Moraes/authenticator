package com.moraes.authenticator.config.security.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.util.MessagesUtil;
import com.moraes.authenticator.config.security.JwtTokenProvider;
import com.moraes.authenticator.config.security.dto.AccountCredentialsDTO;
import com.moraes.authenticator.config.security.dto.TokenDTO;
import com.moraes.authenticator.config.security.interfaces.IAuthService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService implements IAuthService {

    private AuthenticationManager authenticationManager;

    private JwtTokenProvider tokenProvider;

    private IUserService userService;

    @Override
    public TokenDTO signin(AccountCredentialsDTO data) {
        try {
            final String username = data.getUsername();
            final String password = data.getPassword();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return tokenProvider.createAccessToken(username,
                    ((User) userService.loadUserByUsername(username)).getRoles());
        } catch (Exception e) {
            log.warn("signin " + e.getMessage(), e);
            throw new BadCredentialsException(MessagesUtil.getMessage("invalid_username_password"));
        }
    }

    @Override
    public TokenDTO refreshToken(String refreshToken) {
        final TokenDTO token = tokenProvider.refreshToken(refreshToken);
        if (!userService.existsByUsername(token.getUsername())) {
            throw new UsernameNotFoundException(MessagesUtil.getMessage("user_not_found"));
        }
        return token;
    }
}
