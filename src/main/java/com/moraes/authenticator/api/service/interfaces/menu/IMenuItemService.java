package com.moraes.authenticator.api.service.interfaces.menu;

import java.util.List;

import com.moraes.authenticator.api.model.dto.menu.MenuItemDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.MenuItem;

public interface IMenuItemService {

    /**
     * List all items according to permissions and that are active
     *
     * @param  menuItems  the list of MenuItem objects {@link MenuItem}
     * @param  roles  the list of RoleEnum objects {@link RoleEnum}
     * @return       a list of MenuItemDTO objects {@link MenuItemDTO}
     */
    List<MenuItemDTO> findAll(List<MenuItem> menuItems, List<RoleEnum> roles);
}
