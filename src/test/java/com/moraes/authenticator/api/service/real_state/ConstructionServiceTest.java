package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.exception.ValidException;
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
        mockSuperUser();
        when(repository.findByNameAndCompanyKey(anyString(), anyLong())).thenReturn(Optional.of(entity));

        final Optional<Construction> optional = service.getByNameAndCompanyKey("test");

        assertNotNull(optional, "Construction not found");
        assertEquals(entity, optional.get(), "Construction not equal");
    }

    @Test
    void testGetNameAll() {
        mockSuperUser();
        when(repository.findNameByCompanyKeyAndEnabledTrueOrderByName(anyLong()))
                .thenReturn(input.mockNameList(input.getMaxSize()));
        final List<String> list = service.getNameAll();

        assertNotNull(list, "List is null");
        assertEquals(input.getMaxSize(), list.size(), "List size is not equal");
    }

    @Test
    void testGetRepository() {
        assertNotNull(service.getRepository(), "Repository is null");
    }

    @Test
    @DisplayName("Junit Test Given Construction When save Then Return saved Construction")
    void testGivenConstructionWhenSaveThenReturnSavedConstruction() {
        mockSaveSuccess();

        final Construction construction = service.save(entity);

        assertNotNull(construction, "Construction not found");
        assertEquals(entity, construction, "Construction not equal");
    }

    @Test
    @DisplayName("Junit Test Given Construction with duplicate name When save Then Return thrown ValidException")
    void testGivenConstructionWithDuplicateNameWhenSaveThenReturnThrownValidException() {
        mockSuperUser();

        when(repository.existsByKeyNotAndCompanyKeyAndName(anyLong(), anyLong(), anyString())).thenReturn(true);

        final ValidException exception = assertThrows(ValidException.class, () -> service.save(entity),
                "ValidException not thrown");

        assertNotNull(exception, "Exception is null");
        assertEquals(1, exception.getErrs().size(), "Return size of errors is different");
        assertTrue(exception.getErrs().contains("construction.name.duplicate"), "Return errors is different");
    }

    @Test
    void testInsert() {
        mockSaveSuccess();

        final Long constructionKey = service.insert(entity);

        assertNotNull(constructionKey, "Construction not found");
    }

    @Test
    @DisplayName("Junit Test Given real name When getOrInsertByName Then Return Construction")
    void testGivenRealNameWhenGetOrInsertByNameThenReturnConstruction() {
        mockSuperUser();
        when(repository.findByNameAndCompanyKey(anyString(), anyLong())).thenReturn(Optional.of(entity));

        final Construction construction = service.getOrInsertByName("test");

        assertNotNull(construction, "Construction not found");
    }

    @Test
    @DisplayName("Junit Test Given new name When getOrInsertByName Then Return Construction")
    void testGivenNewNameWhenGetOrInsertByNameThenReturnConstruction() {
        mockSaveSuccess();

        final Construction construction = service.getOrInsertByName("test");

        assertNotNull(construction, "Construction not found");
    }

    private void mockSaveSuccess() {
        mockSuperUser();

        when(repository.save(any())).thenReturn(entity);
        when(repository.existsByKeyNotAndCompanyKeyAndName(any(), any(), anyString())).thenReturn(false);
    }

    private void mockSuperUser() {
        mockSecurity.mockSuperUser();
    }
}
