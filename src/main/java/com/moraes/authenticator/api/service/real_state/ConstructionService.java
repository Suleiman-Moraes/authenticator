package com.moraes.authenticator.api.service.real_state;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.repository.IConstructionRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ConstructionService implements IConstructionService {

    @Getter
    private IConstructionRepository repository;
    
}
