package com.moraes.authenticator.api.service.interfaces.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.real_state.Construction;
import com.moraes.authenticator.api.service.interfaces.IServiceInsert;

public interface IConstructionService extends IServiceInsert<Construction, Long> {

    List<String> getNameAll();

    Construction getOrInsertByName(String name);
}
