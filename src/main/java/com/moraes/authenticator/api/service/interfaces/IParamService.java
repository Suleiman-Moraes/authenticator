package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;

public interface IParamService {

    Param findByNameIfNotExistsCreate(ParamEnum paramEnum);
}
