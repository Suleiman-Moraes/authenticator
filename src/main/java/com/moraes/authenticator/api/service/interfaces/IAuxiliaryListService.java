package com.moraes.authenticator.api.service.interfaces;

import java.util.List;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;

public interface IAuxiliaryListService {

    List<KeyDescriptionDTO<String>> getEnumList(String enumName);

    List<KeyDescriptionDTO<String>> getRoleEnumList();
}
