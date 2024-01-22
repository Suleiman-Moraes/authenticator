package com.moraes.authenticator.api.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.User;

public interface IUserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByUsernameAndKeyNot(String username, long key);

    Optional<User> findByUsernameAndPersonEmailAndEnabled(String username, String email, boolean enabled);

    Optional<User> findByTokenResetPasswordAndTokenResetPasswordEnabledAndEnabledAndTokenResetPasswordExpirationDateAfter(
            UUID tokenResetPassword, Boolean tokenResetPasswordEnabled, Boolean enabled, LocalDateTime now);

    Optional<User> findByTokenResetPasswordAndTokenResetPasswordEnabledAndEnabled(UUID tokenResetPassword,
            Boolean tokenResetPasswordEnabled, Boolean enabled);
}
