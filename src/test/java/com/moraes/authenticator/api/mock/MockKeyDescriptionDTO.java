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
}
