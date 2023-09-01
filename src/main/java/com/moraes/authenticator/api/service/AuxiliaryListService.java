package com.moraes.authenticator.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.interfaces.IDescription;
import com.moraes.authenticator.api.service.interfaces.IAuxiliaryListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuxiliaryListService implements IAuxiliaryListService {

    public List<KeyDescriptionDTO<String>> getEnumList(Class<? extends IDescription> clazz) {
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
