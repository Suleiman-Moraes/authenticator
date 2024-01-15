package com.moraes.authenticator.api.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.moraes.authenticator.dto.TestSomeObjectDTO;

class JsonObjectUtilTest {

    @Test
    @DisplayName("Junit test Given Object When convertObjectToMap Then Return Map")
    void testGivenObjectWhenConvertObjectToMapThenReturnMap() {
        final TestSomeObjectDTO someObject = new TestSomeObjectDTO("convertObjectToMap");
        final Map<String, Object> map = JsonObjectUtil.convertObjectToMap(someObject);

        assertNotNull(map, "Map is null");
        assertTrue(map.containsKey("value"), "Map does not contain 'value' key");
        assertEquals(someObject.getValue(), map.get("value"), "Value is different");
    }

    @Test
    @DisplayName("Junit test Given null When convertObjectToMap Then Return Map")
    void testGivenNullWhenConvertObjectToMapThenReturnMap() {
        final Map<String, Object> map = JsonObjectUtil.convertObjectToMap(null);

        assertNull(map, "Map is not null");
    }

    @Test
    @DisplayName("Junit test Given List<Map<String, Object>> When convertMapsToListOfSomething Then Return List<T>")
    void testGivenListMapWhenConvertMapsToListOfSomethingThenReturnList() {
        final List<Map<String, Object>> list = List.of(
                Map.of("value", "convertMapsToListOfSomething"),
                Map.of("value", "convertMapsToListOfSomething2"));

        final List<TestSomeObjectDTO> someObjects = JsonObjectUtil.convertMapsToListOfSomething(list,
                TestSomeObjectDTO.class);

        assertNotNull(someObjects, "List is null");
        assertEquals(2, someObjects.size(), "List size is different");
        assertEquals("convertMapsToListOfSomething", someObjects.get(0).getValue(), "Value is different");
        assertEquals("convertMapsToListOfSomething2", someObjects.get(1).getValue(), "Value is different");
    }

    @Test
    @DisplayName("Junit test Given null When convertMapsToListOfSomething Then Return null")
    void testGivenNullWhenConvertMapsToListOfSomethingThenReturnNull() {
        final List<TestSomeObjectDTO> someObjects = JsonObjectUtil.convertMapsToListOfSomething(null,
                TestSomeObjectDTO.class);

        assertNull(someObjects, "List is not null");
    }
}
