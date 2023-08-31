package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.ProfileDTO;

public interface IProfileService extends IService<Profile, Long> {

    void update(ProfileDTO object, Long key);

    Profile preInsertMe(Profile profile);
}
