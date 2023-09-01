package com.moraes.authenticator.api.service;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.controller.ProfileController;
import com.moraes.authenticator.api.exception.ValidException;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileFilterDTO;
import com.moraes.authenticator.api.repository.IProfileRepository;
import com.moraes.authenticator.api.service.interfaces.IProfileService;
import com.moraes.authenticator.api.util.MessagesUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class ProfileService implements IProfileService {

    private static final Long COMMON_USER_PROFILE_ID = 3L;

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

    @Override
    public Profile preInsertMe(Profile profile) {
        profile.setKey(COMMON_USER_PROFILE_ID);
        return profile;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Page<KeyDescriptionDTO> findPageAll(ProfileFilterDTO filter) {
        if (filter.isPaginate()) {
            final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), filter.getDirection(),
                    filter.getProperty());
            return new PageImpl<>(new LinkedList<>(), pageable, 0);
        }
        final Sort sort = Sort.by(filter.getDirection(), filter.getProperty());
        final List<Profile> list = repository.findAll(sort);
        final Pageable pageable = PageRequest.of(0, list.size(), sort);
        return new PageImpl<>(parseObjects(list,
                KeyDescriptionDTO.class,
                ProfileController.class), pageable,
                list.size());
    }
}
