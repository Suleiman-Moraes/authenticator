package com.moraes.authenticator.api.mock.menu;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.dto.menu.MenuDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.Menu;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockMenu extends AbstractMock<Menu> {

    private List<RoleEnum> roles;
    private List<String> routerLink;

    @Override
    protected Class<Menu> getClazz() {
        return Menu.class;
    }

    @Override
    protected void setOdersValues(Menu entity, Integer number) {
        entity.setRoles(getRoles());
    }

    public List<MenuDTO> mockMenuDTOList() {
        List<MenuDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockMenuDTO(i));
        }
        return entitys;
    }

    public MenuDTO mockMenuDTO(Integer number) {
        try {
            MenuDTO entity = new MenuDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<RoleEnum> getRoles() {
        if (CollectionUtils.isEmpty(roles)) {
            roles = new LinkedList<>();
            roles.add(RoleEnum.ADMIN);
        }
        return roles;
    }

    public List<String> getRouterLink() {
        if (CollectionUtils.isEmpty(routerLink)) {
            routerLink = new LinkedList<>();
            routerLink.add("/pages");
        }
        return routerLink;
    }
}
