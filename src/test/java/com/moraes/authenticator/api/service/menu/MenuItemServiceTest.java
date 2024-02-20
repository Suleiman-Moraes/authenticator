package com.moraes.authenticator.api.service.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.menu.MockMenuItem;
import com.moraes.authenticator.api.model.dto.menu.MenuItemDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.menu.MenuItem;

class MenuItemServiceTest {

    @Spy
    @InjectMocks
    private MenuItemService service;

    private MockMenuItem input;

    @BeforeEach
    void setUp() {
        input = new MockMenuItem();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Junit Test Given Null List When FindAll Then Return Empty List")
    void givenNullListWhenFindAllThenReturnEmptyList() {
        assertEquals(0, service.findAll(null, null).size(), "List is not empty");
    }

    @Test
    @DisplayName("Junit Test Given List of MenuItem And List of Role When FindAll Then Return List of MenuItemDTO")
    void givenListOfMenuItemAndListOfRoleWhenFindAllThenReturnListOfMenuItemDTO() {
        List<MenuItem> items = input.mockEntityList(3);
        items.get(0).setItems(new LinkedList<>());
        items.get(1).setItems(input.mockEntityList(2));
        List<RoleEnum> roles = input.getRoles();
        roles.add(RoleEnum.COMMON_USER);

        List<MenuItemDTO> result = service.findAll(items, roles);

        assertNotNull(result, "List is null");
        assertEquals(3, result.size(), "List size is not equal");
        assertEquals(2, result.get(1).getItems().size(), "List size of second item is not equal");
        assertEquals(0, result.get(0).getItems().size(), "List size of first item is not equal");

        MenuItemDTO firstItem = result.get(0);
        assertEquals("Teste String1", firstItem.getLabel(), "First item label is not equal");
        assertEquals("Teste String1", firstItem.getIcon(), "First item icon is not equal");
        assertEquals("/pages", firstItem.getRouterLink().get(0), "First item routerLink is not equal");

    }

    @Test
    @DisplayName("Junit Test Given List of MenuItem With disabled item And item with other role And List of Role When FindAll Then Return List of MenuItemDTO")
    void givenListOfMenuItemWithDisabledItemAndItemWithOtherRoleAndListOfRoleWhenFindAllThenReturnListOfMenuItemDTO() {
        List<MenuItem> items = input.mockEntityList(3);
        items.get(0).setRoles(null);
        items.get(1).setEnabled(false);

        List<MenuItemDTO> result = service.findAll(items, new LinkedList<>());

        assertNotNull(result, "List is null");
        assertEquals(1, result.size(), "List size is not equal");
        assertEquals(0, result.get(0).getItems().size(), "List size of first item is not equal");

        MenuItemDTO firstItem = result.get(0);
        assertEquals("Teste String1", firstItem.getLabel(), "First item label is not equal");
        assertEquals("Teste String1", firstItem.getIcon(), "First item icon is not equal");
        assertEquals("/pages", firstItem.getRouterLink().get(0), "First item routerLink is not equal");

    }
}
