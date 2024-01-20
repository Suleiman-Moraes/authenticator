package com.moraes.authenticator.api.service;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;
import com.moraes.authenticator.api.repository.IParamRepository;
import com.moraes.authenticator.api.service.interfaces.IParamService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ParamService implements IParamService {

    private final IParamRepository repository;

    @Override
    public Param findByNameIfNotExistsCreate(ParamEnum paramEnum) {
        return repository.findByName(paramEnum).orElseGet(() -> create(paramEnum));
    }

    public Param create(ParamEnum paramEnum) {
        return repository.save(Param.builder()
                .name(paramEnum)
                .value(paramEnum.getDefaultValue())
                .description(paramEnum.getDescription())
                .build());
    }
}
