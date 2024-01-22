package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.mock.MockParam;
import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;
import com.moraes.authenticator.api.repository.IParamRepository;

class ParamServiceTest {

    @Spy
    @InjectMocks
    private ParamService service;

    @Mock
    private IParamRepository repository;

    private MockParam input;

    @BeforeEach
    void setUp() {
        input = new MockParam();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Junit test Given paramEnum When findByNameIfNotExistsCreate Then return Param")
    void testGivenParamEnumWhenFindByNameIfNotExistsCreateThenReturnParam() {
        final ParamEnum paramEnum = ParamEnum.TOKEN_RESET_PASSWORD_EXPIRATION_TIME;

        when(repository.findByName(paramEnum)).thenReturn(Optional.of(input.mockEntity(paramEnum)));

        Param param = service.findByNameIfNotExistsCreate(paramEnum);

        assertNotNull(param, "Param should not be null");
        assertEquals(paramEnum, param.getName(), "Param name should be TOKEN_RESET_PASSWORD_EXPIRATION_TIME");
        assertEquals(paramEnum.getDefaultValue(), param.getValue(), "Param value should be 1 hour");
        assertEquals(paramEnum.getDescription(), param.getDescription(), "Param description is not correct");
    }

    @Test
    void testCreateWithNonNullParamEnum() {
        // Testing the creation of a Param with a non-null ParamEnum
        // Arrange
        ParamEnum paramEnum = ParamEnum.TOKEN_RESET_PASSWORD_EXPIRATION_TIME;
        when(repository.save(any(Param.class))).thenReturn(input.mockEntity(paramEnum));

        // Act
        Param param = service.create(paramEnum);

        // Assert
        assertNotNull(param, "Param should not be null");
        assertEquals(paramEnum, param.getName(), "Param name should be TOKEN_RESET_PASSWORD_EXPIRATION_TIME");
        assertEquals(paramEnum.getDefaultValue(), param.getValue(), "Param value should be 1 hour");
        assertEquals(paramEnum.getDescription(), param.getDescription(), "Param description is not correct");
    }
}
