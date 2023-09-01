package com.moraes.authenticator.api.service.interfaces;

import java.util.List;

import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.interfaces.IDescription;

public interface IAuxiliaryListService {

    List<KeyDescriptionDTO<String>> getEnumList(Class<? extends IDescription> clazz);
}
