package com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndKeyNot(String username, long key);
}
