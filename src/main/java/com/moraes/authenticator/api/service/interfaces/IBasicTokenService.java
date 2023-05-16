package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.BasicToken;

public interface IBasicTokenService extends IServiceFindAll<BasicToken, Long> {

    boolean validateBasicToken();
}
