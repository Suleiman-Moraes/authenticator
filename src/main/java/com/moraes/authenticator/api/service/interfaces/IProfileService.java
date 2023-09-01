package com.moraes.authenticator.api.service.interfaces;

import org.springframework.data.domain.Page;

import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.KeyDescriptionDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.dto.profile.ProfileFilterDTO;

public interface IProfileService extends IService<Profile, Long> {

    void update(ProfileDTO object, Long key);

    Profile preInsertMe(Profile profile);

    @SuppressWarnings("rawtypes")
    Page<KeyDescriptionDTO> findPageAll(ProfileFilterDTO filter);
}
