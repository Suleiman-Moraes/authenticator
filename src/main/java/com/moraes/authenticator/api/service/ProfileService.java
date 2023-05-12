package com.moraes.authenticator.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.ProfileDTO;
import com.moraes.authenticator.api.repository.IProfileRepository;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.util.MessagesUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ProfileService implements IProfileService {

    @Getter
    private IProfileRepository repository;

    @Transactional
    @Override
    public void delete(Long id) {
        final Profile entity = findByKey(id);
        if (CollectionUtils.isEmpty(entity.getUsers())) {
            repository.delete(entity);
        } else {
            throw new ValidException(MessagesUtil.getMessage("there_are_users_linked_this_profile"));
        }
    }

    @Override
    public void update(ProfileDTO object, Long key) {
        Profile entity = findByKey(key);
        entity.setDescription(object.getDescription());
        entity.setRoles(object.getRoles());
        repository.save(entity);
    }
}
