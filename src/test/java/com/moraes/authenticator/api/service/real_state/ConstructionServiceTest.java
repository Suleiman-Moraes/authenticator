package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.real_state.MockConstruction;
import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.repository.IConstructionRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;

class ConstructionServiceTest {

    private static final Long KEY = 1L;

    private MockConstruction input;
    private MockSecurity mockSecurity;

    @Spy
    @InjectMocks
    private ConstructionService service;

    @Mock
    private IConstructionRepository repository;

    @Mock
    private IUserService userService;

    private Construction entity;

    @BeforeEach
    void setUp() {
        input = new MockConstruction();
        mockSecurity = new MockSecurity();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testGetByNameAndCompanyKey() {
        mockUserServiceGetMe();
        when(repository.findByNameAndCompanyKey(anyString(), anyLong())).thenReturn(Optional.of(entity));

        Optional<Construction> optional = service.getByNameAndCompanyKey("test");

        assertNotNull(optional, "Construction not found");
        assertEquals(entity, optional.get(), "Construction not equal");
    }

    @Test
    void testGetNameAll() {
        mockUserServiceGetMe();
        when(repository.findNameByCompanyKeyAndEnabledTrueOrderByName(anyLong()))
                .thenReturn(input.mockNameList(input.getMaxSize()));
        List<String> list = service.getNameAll();

        assertNotNull(list, "List is null");
        assertEquals(input.getMaxSize(), list.size(), "List size is not equal");
    }

    private void mockUserServiceGetMe() {
        mockSecurity.mockSuperUser();
    }

    @Test
    void testGetRepository() {
        assertNotNull(service.getRepository(), "Repository is null");
    }
}
