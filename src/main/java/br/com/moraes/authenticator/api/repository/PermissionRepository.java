package br.com.moraes.authenticator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moraes.authenticator.api.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Permission findTopByAuthority(String authority);
}