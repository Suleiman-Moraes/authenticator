package com.moraes.authenticator.api.service.real_state;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mock.MockSecurity;
import com.moraes.authenticator.api.mock.real_state.MockEnterprise;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import com.moraes.authenticator.api.repository.IEnterpriseRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.service.interfaces.real_state.IConstructionService;

class EnterpriseServiceTest {

    private static final Long KEY = 1L;

    private MockEnterprise input;
    private MockSecurity mockSecurity;

    @Spy
    @InjectMocks
    private EnterpriseService service;

    @Mock
    private IEnterpriseRepository repository;

    @Mock
    private IUserService userService;

    @Mock
    private IConstructionService constructionService;;

    private Enterprise entity;

    @BeforeEach
    void setUp() {
        input = new MockEnterprise();
        mockSecurity = new MockSecurity();
        MockitoAnnotations.openMocks(this);

        entity = input.mockEntity(1);
        entity.setKey(KEY);
    }

    @Test
    void testGetRepository() {
        assertNotNull(service.getRepository(), "Repository is null");
    }

    @Test
    void testGetNameByConstructionName() {
        mockSuperUser();
        when(repository.findDistinctNameByConstructionNameAndConstructionCompanyKeyAndEnabledTrueOrderByName(
                anyString(), anyLong())).thenReturn(input.mockNameList(input.getMaxSize()));

        final List<String> list = service.getNameByConstructionName("Test");

        assertNotNull(list, "List is null");
        assertEquals(input.getMaxSize(), list.size(), "List size is not equal");
    }

    @DisplayName("JUnit test Given name and constructionName When getByNameAndConstructionName Then return Enterprise")
    @Test
    void testGivenNameAndConstructionNameWhenGetByNameAndConstructionNameThenReturnEnterprise() {
        mockSuperUser();

        when(repository.findTopByNameAndConstructionNameAndConstructionCompanyKeyAndEnabledTrue(anyString(),
                anyString(), anyLong())).thenReturn(Optional.of(entity));

        final Enterprise enterprise = service.getByNameAndConstructionName("Test", "Test");

        assertNotNull(enterprise, "Enterprise not found");
        assertEquals(entity, enterprise, "Enterprise not equal");
    }

    @DisplayName("JUnit test Given fake name and fake constructionName When getByNameAndConstructionName Then throw ResourceNotFoundException")
    @Test
    void testGivenFakeNameAndFakeConstructionNameWhenGetByNameAndConstructionNameThenThrowResourceNotFoundException() {
        mockSuperUser();

        when(repository.findTopByNameAndConstructionNameAndConstructionCompanyKeyAndEnabledTrue(anyString(),
                anyString(), anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getByNameAndConstructionName("Test", "Test"));
    }

    @Test
    @DisplayName("JUnit test Given Enterprise and constructionName When insert Then return Long")
    void testGivenEnterpriseAndConstructionNameWhenInsertThenReturnLong() {
        assertEquals(KEY, service.insert(entity, "Test"), "Key not equal");
    }

    @Test
    void testInsert() {
        assertEquals(KEY, service.insert(entity), "Key not equal");
    }

    @ParameterizedTest(name = "{index} => dto={0}, entity={1}")
    @MethodSource("provideParametersParseOtherFields")
    @DisplayName("JUnit test Given multiple values of EnterpriseDTO and Enterprise When parseOtherFields Then return EnterpriseDTO")
    void testGivenMultipleValuesOfEnterpriseDTOAndEnterpriseWhenParseOtherFieldsThenReturnEnterpriseDTO(
            EnterpriseDTO dto, Enterprise entity) {
        final EnterpriseDTO enterpriseDTO = service.parseOtherFields(dto, entity);

        if (dto != null) {
            verify(dto, never()).setConstructionName(any());
        }
        assertEquals(dto, enterpriseDTO, "EnterpriseDTO not equal");
    }

    @Test
    @DisplayName("JUnit test Given EnterpriseDTO and Enterprise When parseOtherFields Then return EnterpriseDTO")
    void testGivenEnterpriseDTOAndEnterpriseWhenParseOtherFieldsThenReturnEnterpriseDTO() {
        EnterpriseDTO dto = mock(EnterpriseDTO.class);
        dto.setConstructionName(null);
        final EnterpriseDTO enterpriseDTO = service.parseOtherFields(dto, entity);

        verify(dto, times(1)).setConstructionName(entity.getConstruction().getName());
        assertEquals(dto, enterpriseDTO, "EnterpriseDTO not equal");
    }

    private static Stream<Arguments> provideParametersParseOtherFields() {
        EnterpriseDTO dto = mock(EnterpriseDTO.class);
        Enterprise entityWithNullConstruction = mock(Enterprise.class);
        when(entityWithNullConstruction.getConstruction()).thenReturn(null);

        Enterprise entityWithConstruction = mock(Enterprise.class);
        Construction construction = mock(Construction.class);
        when(entityWithConstruction.getConstruction()).thenReturn(construction);

        return Stream.of(
                Arguments.of(null, null), // Both dto and entity are null
                Arguments.of(null, entityWithNullConstruction), // dto is null
                Arguments.of(dto, null), // entity is null
                Arguments.of(dto, entityWithNullConstruction) // entity.getConstruction() is null
        );
    }

    private void mockSuperUser() {
        mockSecurity.mockSuperUser();
    }
}
