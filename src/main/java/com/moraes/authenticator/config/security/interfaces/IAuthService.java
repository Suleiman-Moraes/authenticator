package com.moraes.authenticator.config.security.interfaces;

import com.moraes.authenticator.config.security.dto.AccountCredentialsDTO;
import com.moraes.authenticator.config.security.dto.TokenDTO;

public interface IAuthService {
    
    TokenDTO signin(AccountCredentialsDTO data);

    TokenDTO refreshToken(String refreshToken);
}
