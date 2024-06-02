package com.moraes.authenticator.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.enums.FrequencyEnum;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.model.enums.YesNotEnum;
import com.moraes.authenticator.api.model.interfaces.IDescription;
import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuxiliaryListService implements IAuxiliaryListService {

    @Override
    public List<KeyDescriptionDTO<String>> getRoleEnumList() {
        return getEnumListByClass(RoleEnum.class).stream().filter(r -> !RoleEnum.ROOT.name().equals(r.getKey()))
                .collect(Collectors.toList());
    }

    @Override
    public List<KeyDescriptionDTO<String>> getEnumList(String enumName) {
        if (!StringUtils.hasText(enumName)) {
            return null;
        }
        switch (enumName.toUpperCase()) {
            case "FREQUENCYENUM":
                return getEnumListByClass(FrequencyEnum.class);
            case "YESNOTENUM":
                return getEnumListByClass(YesNotEnum.class);
            default:
                return null;
        }
    }

    public List<KeyDescriptionDTO<String>> getEnumListByClass(Class<? extends IDescription> clazz) {
        try {
            IDescription[] vet = clazz.getEnumConstants();
            List<KeyDescriptionDTO<String>> list = new ArrayList<>(vet.length);
            for (IDescription item : vet) {
                list.add(KeyDescriptionDTO.<String>builder()
                        .key(item.toString())
                        .description(item.getDescription())
                        .build());
            }
            list.sort((a, b) -> a.getDescription().compareTo(b.getDescription()));
            return list;
        } catch (Exception e) {
            log.warn("getEnumList " + e.getMessage(), e);
        }
        return null;
    }
}
