package com.moraes.authenticator.api.service.real_state;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.repository.IConstructionRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;
import com.moraes.authenticator.api.util.SecurityUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ConstructionService implements IConstructionService {

    @Getter
    private IConstructionRepository repository;

    public List<String> getNameAll() {
        return repository.findNameByCompanyKeyAndEnabledTrueOrderByName(SecurityUtil.getPrincipalOrThrow().getKey());
    }

    public Optional<Construction> getByNameAndCompanyKey(String name) {
        return repository.findByNameAndCompanyKey(name, SecurityUtil.getPrincipalOrThrow().getKey());
    }
}
