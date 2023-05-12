package com.moraes.authenticator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.Profile;

public interface IProfileRepository extends JpaRepository<Profile, Long> {

}
