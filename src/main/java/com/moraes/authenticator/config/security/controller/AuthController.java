package com.moraes.authenticator.config.security.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.moraes.authenticator.api.service.interfaces.IBasicTokenService;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.api.util.MessagesUtil;
import com.moraes.authenticator.config.security.dto.AccountCredentialsDTO;
import com.moraes.authenticator.config.security.dto.TokenDTO;
import com.moraes.authenticator.config.security.interfaces.IAuthService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private IAuthService authService;

    private IBasicTokenService basicTokenService;

    @PostMapping(value = "/signin")
    public ResponseEntity<Object> signin(@RequestBody AccountCredentialsDTO data) {
        basicTokenService.validateBasicToken();
        if (data != null && StringUtils.hasText(data.getUsername()) && StringUtils.hasText(data.getPassword())) {
            final TokenDTO token = authService.signin(data);
            if (token != null) {
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessagesUtil.getMessage("invalid_client_request"));
    }

    @PutMapping(value = "/refresh")
    public ResponseEntity<Object> refreshToken(@RequestHeader(ConstantsUtil.AUTHORIZATION) String refreshToken) {
        if (StringUtils.hasText(refreshToken)) {
            TokenDTO token = authService.refreshToken(refreshToken);
            if (token != null) {
                token.setRefreshToken(null);
                return ResponseEntity.ok(token);
            }
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessagesUtil.getMessage("invalid_client_request"));
    }
}
