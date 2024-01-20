package com.moraes.authenticator.api.mock;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;

public class MockParam {

    public Param mockEntity(ParamEnum paramEnum) {
        return Param.builder()
                .name(paramEnum)
                .description(paramEnum.getDescription())
                .value(paramEnum.getDefaultValue())
                .build();
    }
}
