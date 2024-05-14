package com.moraes.authenticator.api.service.interfaces.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.real_state.Construction;

public interface IConstructionService {

    List<String> getNameAll();

    Construction getOrInsertByName(String name);
}
