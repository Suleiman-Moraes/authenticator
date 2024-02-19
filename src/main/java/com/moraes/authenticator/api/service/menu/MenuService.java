package com.moraes.authenticator.api.service.menu;

import java.util.LinkedList;
import java.util.List;

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
        List<MenuDTO> dtos = new LinkedList<>();
        final List<RoleEnum> roles = SecurityUtil.getRoles();
        final List<Menu> menus = repository.findByEnabledTrueAndRolesIn(roles);
        if (!CollectionUtils.isEmpty(menus)) {
            menus.forEach(menu -> {
                final MenuDTO dto = MenuDTO.builder()
                        .label(menu.getLabel())
                        .items(menuItemService.findAll(menu.getItems(), roles))
                        .build();
                dtos.add(dto);
            });
        }
        return dtos;
    }
}
