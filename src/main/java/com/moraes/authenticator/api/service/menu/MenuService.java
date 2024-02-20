package com.moraes.authenticator.api.service.menu;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.model.dto.menu.MenuDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.Menu;
import com.moraes.authenticator.api.repository.IMenuRepository;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuItemService;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuService;
import com.moraes.authenticator.api.util.SecurityUtil;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class MenuService implements IMenuService {

    private final IMenuRepository repository;

    private final IMenuItemService menuItemService;

    @Transactional(readOnly = true)
    public List<MenuDTO> findAll() {
        final List<RoleEnum> roles = Optional.ofNullable(SecurityUtil.getRoles()).orElse(new LinkedList<>());
        final List<Menu> menus = repository.findByEnabledTrueAndRolesIn(roles);
        return CollectionUtils.isEmpty(menus) ? new LinkedList<>()
                : menus.stream().map(menu -> MenuDTO.builder()
                        .label(menu.getLabel())
                        .items(menuItemService.findAll(menu.getItems(), roles))
                        .build()).toList();
    }
}
