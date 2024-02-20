package com.moraes.authenticator.api.service.menu;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.model.dto.menu.MenuItemDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.MenuItem;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuItemService;

@Service
public class MenuItemService implements IMenuItemService {

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDTO> findAll(List<MenuItem> items, List<RoleEnum> roles) {
        if (!CollectionUtils.isEmpty(items)) {
            List<MenuItemDTO> dtos = new LinkedList<>();
            items.forEach(item -> {
                if (Boolean.TRUE.equals(item.getEnabled())
                        && (CollectionUtils.isEmpty(item.getRoles()) || roles.stream()
                                .anyMatch(role -> item.getRoles().contains(role)))) {
                    dtos.add(MenuItemDTO.builder()
                            .icon(item.getIcon())
                            .label(item.getLabel())
                            .routerLink(item.getRouterLink())
                            .items(findAll(item.getItems(), roles))
                            .build());
                }
            });
            return dtos;
        }
        return null;
    }
}
