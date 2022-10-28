package br.com.moraes.authenticator.api.service;

import br.com.moraes.authenticator.api.enums.DynamicPropertyEnum;
import br.com.moraes.authenticator.api.model.DynamicProperty;

public interface DynamicPropertyService {

	DynamicProperty create(DynamicProperty dynamicProperty);

	DynamicProperty findById(DynamicPropertyEnum dynamicPropertyEnum);

	boolean existsById(DynamicPropertyEnum dynamicPropertyEnum);
}
