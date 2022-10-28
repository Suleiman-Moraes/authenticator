package br.com.moraes.authenticator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moraes.authenticator.api.enums.DynamicPropertyEnum;
import br.com.moraes.authenticator.api.model.DynamicProperty;

public interface DynamicPropertyRepository extends JpaRepository<DynamicProperty, DynamicPropertyEnum> {

}
