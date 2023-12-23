package com.moraes.authenticator.api.mock;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;

public class MockKeyDescriptionDTO {

    public KeyDescriptionDTO<String> mockString(String key) {
        return KeyDescriptionDTO.<String>builder()
                .key(key)
                .description(key).build();
    }

    public List<KeyDescriptionDTO<String>> mockStringList() {
        List<KeyDescriptionDTO<String>> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockString(RandomStringUtils.randomAlphabetic(5)));
        }
        return entitys;
    }

    public KeyDescriptionDTO<Long> mockString(Long key) {
        return KeyDescriptionDTO.<Long>builder()
                .key(key)
                .description("description" + key).build();
    }

    public List<KeyDescriptionDTO<Long>> mockLongList() {
        List<KeyDescriptionDTO<Long>> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockString(Long.valueOf(i)));
        }
        return entitys;
    }

    @SuppressWarnings("rawtypes")
    public KeyDescriptionDTO mock(Object key) {
        return KeyDescriptionDTO.builder()
                .key(key)
                .description(key.toString()).build();
    }

    @SuppressWarnings("rawtypes")
    public List<KeyDescriptionDTO> mockList() {
        List<KeyDescriptionDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mock(Long.valueOf(i)));
        }
        return entitys;
    }
}
