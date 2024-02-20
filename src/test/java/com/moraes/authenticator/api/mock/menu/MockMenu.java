package com.moraes.authenticator.api.mock.menu;

import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.Menu;

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
