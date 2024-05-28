package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.enums.YesNotEnum;

class AuxiliaryListServiceTest {

    @Spy
    @InjectMocks
    private AuxiliaryListService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEnumListByClass() {
        var list = Arrays.asList(KeyDescriptionDTO.<String>builder()
                .key(YesNotEnum.NOT.toString())
                .description(YesNotEnum.NOT.getDescription())
                .build(),
                KeyDescriptionDTO.<String>builder()
                        .key(YesNotEnum.YES.toString())
                        .description(YesNotEnum.YES.getDescription())
                        .build());
        assertEquals(list, service.getEnumListByClass(YesNotEnum.class), "Return not equal");
    }

    @Test
    @DisplayName("JUnit test null Class when getEnumListByClass Then return null")
    void testNullClassWhenGetEnumListByClassThenReturnNull() {
        assertNull(service.getEnumListByClass(null), "Return not null");
    }

    @Test
    @DisplayName("JUnit test Given context When get list role enum Then return list")
    void testGivenContextWhenGetListRoleEnumThenReturnList() {
        final List<KeyDescriptionDTO<String>> list = service.getRoleEnumList();

        assertEquals(2, list.size(), "Size is different");
        assertEquals("Admin", list.get(0).getDescription(), "Description is different");
        assertEquals("ADMIN", list.get(0).getKey(), "Key is different");
        assertEquals("Common user", list.get(1).getDescription(), "Description is different");
        assertEquals("COMMON_USER", list.get(1).getKey(), "Key is different");
    }

    @Test
    @DisplayName("JUnit test Given null When getEnumList Then return null")
    void testGivenNullWhenGetEnumListThenReturnNull() {
        assertNull(service.getEnumList(null), "Return not null");
    }

    @ParameterizedTest
    @ValueSource(strings = { "Wrong", " " })
    @DisplayName("JUnit test Given wrongs values When getEnumList Then return null")
    void testGivenWrongValueWhenGetEnumListThenReturnNull(String enumName) {
        assertNull(service.getEnumList(enumName), "Return not null");
    }

    @ParameterizedTest(name = "{index} => enumName={0}, size={1}")
    @MethodSource("provideParametersGetEnumList")
    @DisplayName("JUnit test Given multiple values When getEnumList Then return list")
    void testGivenMultipleValuesWhenGetEnumListThenReturnList(String enumName, int size) {
        final List<KeyDescriptionDTO<String>> list = service.getEnumList(enumName);

        assertNotNull(list, "Return null");
        assertEquals(size, list.size(), "Size is different");
    }

    private static Stream<Arguments> provideParametersGetEnumList() {
        return Stream.of(
                Arguments.of("FrequencyEnum", 6),
                Arguments.of("YesNotEnum", 2));
    }
}
