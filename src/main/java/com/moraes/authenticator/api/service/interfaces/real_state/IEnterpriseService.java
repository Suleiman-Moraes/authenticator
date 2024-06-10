package com.moraes.authenticator.api.service.interfaces.real_state;

import java.util.List;

import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;
import com.moraes.authenticator.api.model.real_state.Enterprise;
import com.moraes.authenticator.api.service.interfaces.IServiceInsert;

public interface IEnterpriseService extends IServiceInsert<Enterprise, Long> {

    List<String> getNameByConstructionName(String constructionName);

    Enterprise getByNameAndConstructionName(String name, String constructionName);

    Long insert(Enterprise object, String constructionName);

    EnterpriseDTO parseOtherFields(EnterpriseDTO dto, Enterprise entity);
}
