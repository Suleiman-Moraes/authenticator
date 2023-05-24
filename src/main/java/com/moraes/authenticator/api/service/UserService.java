package com.moraes.authenticator.api.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.ExceptionUtilDTO;
import com.moraes.authenticator.api.model.dto.UserDTO;
import com.moraes.authenticator.api.repository.IUserRepository;
import com.moraes.authenticator.api.service.interfaces.IUserService;
import com.moraes.authenticator.api.util.MessagesUtil;
import com.moraes.authenticator.api.util.ConstantsUtil;
import com.moraes.authenticator.api.util.ExceptionsUtil;

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
        valid(key, object.getProfile().getKey(), object.getUsername());
        User entity = findByKey(key);
        entity.setUsername(object.getUsername());
        entity.setProfile(Profile.builder().key(object.getProfile().getKey()).build());
        save(entity);
    }

    @Transactional
    @Override
    public Long insert(User entity, Long personKey) {
        entity.setPerson(Person.builder().key(personKey).build());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        save(entity);
        return entity.getKey();
    }

    @Override
    public User getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (User) authentication.getPrincipal();
        }
        return null;
    }

    @Override
    public void validInsert(User entity) {
        if (!StringUtils.hasText(entity.getPassword())) {
            throw new ValidException(
                    MessagesUtil.getMessage(MessagesUtil.NOT_BLANK, MessagesUtil.getMessage("user.password")));
        }
        valid(entity.getKey(), entity.getProfile().getKey(), entity.getUsername());
    }

    /**
     * Only pass fields not objects 
     * as you can use with Entity and DTO
     * @param key
     * @param profileKey
     * @param username
     */
    public void valid(Long key, Long profileKey, String username) {
        key = key != null ? key : 0;
        ExceptionsUtil.throwValidExceptions(
                ExceptionUtilDTO.builder()
                        .condition(!repository.existsByUsernameAndKeyNot(username,
                                key))
                        .messageKey("user.username.unique")
                        .build(),
                ExceptionUtilDTO.builder()
                        .condition(ConstantsUtil.ONE.longValue() != profileKey
                                || ConstantsUtil.ONE.longValue() == key)
                        .messageKey("user.profile.unavailable")
                        .build());
    }

    private void save(User entity) {
        repository.save(entity);
    }
}
