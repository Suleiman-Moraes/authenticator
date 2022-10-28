package br.com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.moraes.authenticator.api.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameAndIdNot(String username, Long id);

    @Query("SELECT id FROM User WHERE username = ?1")
    long findIdByUsername(String username);

    Optional<User> findTopByUsername(String username);

    boolean existsByUsername(String username);
}