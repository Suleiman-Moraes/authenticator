package com.moraes.authenticator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.real_state.Construction;

public interface IConstructionRepository extends JpaRepository<Construction, Long> {

}
