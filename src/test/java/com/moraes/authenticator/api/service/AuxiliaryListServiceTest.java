package com.moraes.authenticator.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    void testGetEnumList() {
        var list = Arrays.asList(KeyDescriptionDTO.<String>builder()
                .key(YesNotEnum.NOT.toString())
                .description(YesNotEnum.NOT.getDescription())
                .build(),
                KeyDescriptionDTO.<String>builder()
                        .key(YesNotEnum.YES.toString())
                        .description(YesNotEnum.YES.getDescription())
                        .build());
        assertEquals(list, service.getEnumList(YesNotEnum.class), "Return not equal");
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
}
