package com.moraes.authenticator.api.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public Page<KeyDescriptionDTO> findPageAll(ProfileFilterDTO filter) {
        final Map<String, Class<?>> fields = getMapOfFields();
        Page<KeyDescriptionDTO> page = repository.page(filter, fields, KeyDescriptionDTO.class, Profile.class);
        page.getContent().forEach(dto -> addLinks(dto, (long) dto.getKey(), ProfileController.class));
        return page;
    }

    public Map<String, Class<?>> getMapOfFields() {
        final Map<String, Class<?>> fields = new LinkedHashMap<>();
        fields.put("x.key", Number.class);
        fields.put("x.description", String.class);
        return fields;
    }
}
