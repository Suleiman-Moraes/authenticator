package br.com.moraes.authenticator.api.service;

import br.com.moraes.authenticator.api.interfaces.ICrudPatternService;
import br.com.moraes.authenticator.api.model.Permission;

public interface PermissionService extends ICrudPatternService<Permission> {

    Permission findTopByAuthority(String authority);
}