package br.com.moraes.authenticator.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.moraes.authenticator.api.enums.DynamicPropertyEnum;
import br.com.moraes.authenticator.api.model.DynamicProperty;
import br.com.moraes.authenticator.api.repository.DynamicPropertyRepository;
import br.com.moraes.authenticator.api.service.DynamicPropertyService;

@Service
public class DynamicPropertyServiceImpl implements DynamicPropertyService {

	@Autowired
	private DynamicPropertyRepository repository;

	@Override
	public DynamicProperty create(DynamicProperty dynamicProperty) {
		return save(save(dynamicProperty));
	}

	@Override
	public boolean existsById(DynamicPropertyEnum dynamicPropertyEnum) {
		return repository.existsById(dynamicPropertyEnum);
	}

	@Override
	public DynamicProperty findById(DynamicPropertyEnum dynamicPropertyEnum) {
		return repository.findById(dynamicPropertyEnum).orElse(DynamicProperty.builder().key(dynamicPropertyEnum)
				.value(dynamicPropertyEnum.getDefaultValue()).build());
	}

	private DynamicProperty save(DynamicProperty dynamicProperty) {
		return repository.save(dynamicProperty);
	}
}
