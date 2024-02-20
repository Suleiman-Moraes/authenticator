package com.moraes.authenticator.api.service.menu;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.menu.MockMenuItem;

public class MenuItemServiceTest {

    @Spy
    @InjectMocks
    private MenuItemService service;

    private MockMenuItem input;

    @BeforeEach
    void setUp() {
        input = new MockMenuItem();
        MockitoAnnotations.openMocks(this);
    }
}
