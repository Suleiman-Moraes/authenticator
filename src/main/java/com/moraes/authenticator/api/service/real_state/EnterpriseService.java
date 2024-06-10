package com.moraes.authenticator.api.service.real_state;

import static com.moraes.authenticator.api.util.SecurityUtil.getCompanyPrincipalOrThrow;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import com.moraes.authenticator.api.repository.IEnterpriseRepository;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;
import com.moraes.authenticator.api.service.interfaces.real_state.IEnterpriseService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class EnterpriseService implements IEnterpriseService {

    @Getter
    private final IEnterpriseRepository repository;

    private final IConstructionService constructionService;

    @Override
    public List<String> getNameByConstructionName(String constructionName) {
        return repository.findDistinctNameByConstructionNameAndConstructionCompanyKeyAndEnabledTrueOrderByName(
                constructionName, getCompanyPrincipalOrThrow().getKey());
    }

    @Override
    public Enterprise getByNameAndConstructionName(String name, String constructionName) {
        return repository.findTopByNameAndConstructionNameAndConstructionCompanyKeyAndEnabledTrue(name,
                constructionName, getCompanyPrincipalOrThrow().getKey()).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Long insert(Enterprise object, String constructionName) {
        object.setConstruction(constructionService.getOrInsertByName(constructionName));
        return insert(object);
    }

    @Override
    @Transactional(readOnly = true)
    public EnterpriseDTO parseOtherFields(EnterpriseDTO dto, Enterprise entity) {
        if (dto != null && entity != null && entity.getConstruction() != null) {
            dto.setConstructionName(entity.getConstruction().getName());
        }
        return dto;
    }
}
