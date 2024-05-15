package com.moraes.authenticator.api.service.real_state;

import static com.moraes.authenticator.api.util.SecurityUtil.getPrincipalOrThrow;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.Company;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.repository.IConstructionRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;
import com.moraes.authenticator.api.util.ExceptionsUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ConstructionService implements IConstructionService {

    @Getter
    private IConstructionRepository repository;

    @Override
    public List<String> getNameAll() {
        return repository.findNameByCompanyKeyAndEnabledTrueOrderByName(getPrincipalOrThrow().getKey());
    }

    @Override
    public Construction getOrInsertByName(String name) {
        return getByNameAndCompanyKey(name).orElseGet(() -> {
            Construction construction = Construction.builder().name(name).build();
            insert(construction);
            return construction;
        });
    }

    public Optional<Construction> getByNameAndCompanyKey(String name) {
        return repository.findByNameAndCompanyKey(name, getPrincipalOrThrow().getKey());
    }

    @Override
    public Long insert(Construction entity) {
        return save(entity).getKey();
    }

    public Construction save(Construction entity) {
        entity.setCompany(Company.builder().key(getPrincipalOrThrow().getKey()).build());

        ExceptionsUtil.throwValidExceptions(ExceptionUtilDTO.builder()
                .condition(repository.existsByKeyNotAndCompanyKeyAndName(entity.getKey(), entity.getCompany().getKey(),
                        entity.getName()))
                .messageKey("construction.name.duplicate")
                .build());

        return repository.save(entity);
    }
}
