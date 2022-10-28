package br.com.moraes.authenticator.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moraes.authenticator.api.model.Permission;
import br.com.moraes.authenticator.api.repository.PermissionRepository;
import br.com.moraes.authenticator.api.service.PermissionService;
import br.com.moraes.authenticator.api.service.abstracts.CrudPatternServiceImpl;
import lombok.Getter;

@Service
public class PermissionServiceImpl extends CrudPatternServiceImpl<Permission> implements PermissionService {

    @Autowired
    @Getter
    private PermissionRepository repository;

    @Override
    public Permission findTopByAuthority(String authority) {
        return repository.findTopByAuthority(authority);
    }
}