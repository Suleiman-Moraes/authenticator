package com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.BasicToken;

public interface IBasicTokenRepository extends JpaRepository<BasicToken, Long> {

    Optional<BasicToken> findByUsername(String username);

}
