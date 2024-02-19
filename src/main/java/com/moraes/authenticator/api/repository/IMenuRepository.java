package com.moraes.authenticator.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.Menu;

public interface IMenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByEnabledTrueAndRolesIn(List<RoleEnum> roles);
}
