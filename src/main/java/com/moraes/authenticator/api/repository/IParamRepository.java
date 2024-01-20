package com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.enums.ParamEnum;

public interface IParamRepository extends JpaRepository<Param, Long> {

    Optional<Param> findByName(ParamEnum paramEnum);
}
