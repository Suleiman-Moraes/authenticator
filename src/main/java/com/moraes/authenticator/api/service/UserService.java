package com.moraes.authenticator.api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.UserDTO;
import com.moraes.authenticator.api.repository.IUserRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.util.MessagesUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@AllArgsConstructor
public class UserService implements IUserService {

    @Getter
    private IUserRepository repository;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessagesUtil.getMessage("user_not_found")));
    }

    @Override
    public boolean existsByUsername(String username) {
        return repository.existsByUsername(username);
    }

    @Transactional
    @Override
    public void update(UserDTO object, Long key) {
        User entity = findByKey(key);
        entity.setUsername(object.getUsername());
        entity.setProfile(Profile.builder().key(object.getProfile().getKey()).build());
        repository.save(entity);
    }

    @Transactional
    @Override
    public Long insert(User entity, Long personKey) {
        entity.setPerson(Person.builder().key(personKey).build());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        repository.save(entity);
        return entity.getKey();
    }
}
