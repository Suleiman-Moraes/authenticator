package com.moraes.authenticator.api.service.menu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.menu.MockMenu;
import com.moraes.authenticator.api.model.dto.menu.MenuDTO;
import com.moraes.authenticator.api.repository.IMenuRepository;
import com.moraes.authenticator.api.service.interfaces.menu.IMenuItemService;

class MenuServiceTest {

    @Spy
    @InjectMocks
    private MenuService service;

    @Mock
    private IMenuRepository repository;

    @Mock
    private IMenuItemService menuItemService;

    private MockMenu input;
    private MockSecurity mockSecurity;

    @BeforeEach
    void setUp() {
        input = new MockMenu();
        mockSecurity = new MockSecurity();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Junit Test Given Context When FindAll Then Return List")
    void givenContextWhenFindAllThenReturnList() {
        mockSecurity.mockSuperUser();
        given(repository.findByEnabledTrueAndRolesIn(anyList())).willReturn(input.mockEntityList());

        List<MenuDTO> result = service.findAll();

        assertNotNull(result, "List is null");
        assertEquals(14, result.size(), "List size is different from 1");

        MenuDTO firstItem = result.get(0);
        assertEquals("Teste String1", firstItem.getLabel(), "First item label is not equal");
    }

    @Test
    @DisplayName("Junit Test Given Context with null role and null menu When FindAll Then Return Empty List")
    void givenContextWithNullRolesAndMenuWithoutRolesWhenFindAllThenReturnList() {
        given(repository.findByEnabledTrueAndRolesIn(anyList())).willReturn(new LinkedList<>());

        List<MenuDTO> result = service.findAll();

        assertNotNull(result, "List is null");
        assertEquals(0, result.size(), "List size is different from 1");
    }
}
