package br.com.moraes.authenticator.api.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;

import br.com.moraes.authenticator.api.dto.ObjectWithIdDTO;
import br.com.moraes.authenticator.api.interfaces.ICrudPatternService;
import br.com.moraes.authenticator.api.model.User;

public interface UserService extends ICrudPatternService<User>, UserDetailsService {

    ObjectWithIdDTO findByToken();

    Optional<User> findTopByUsername(String username);

    boolean existsByUsername(String username);
}