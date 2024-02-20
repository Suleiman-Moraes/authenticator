package com.moraes.authenticator.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.Menu;

public interface IMenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m LEFT OUTER JOIN m.roles role WHERE m.enabled = true AND (role IN :roles OR role IS NULL) GROUP BY m")
    List<Menu> findByEnabledTrueAndRolesIn(List<RoleEnum> roles);
}
