package com.moraes.authenticator.api.service.interfaces.menu;

import java.util.List;

import com.moraes.authenticator.api.model.dto.menu.MenuDTO;

public interface IMenuService {

    /**
     * List all items according to permissions and that are active
     *
     * @return {@link List} of {@link MenuDTO} objects
     */
    List<MenuDTO> findAll();
}
