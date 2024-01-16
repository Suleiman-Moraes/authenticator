package com.moraes.authenticator.api.mock;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.profile.ProfileDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockProfile extends AbstractMock<Profile> {

    private Set<RoleEnum> roles;

    @Override
    protected Class<Profile> getClazz() {
        return Profile.class;
    }

    @Override
    protected void setOdersValues(Profile entity, Integer number) {
        entity.setRoles(getRoles());
    }

    public ProfileDTO mockProfileDTO(Integer number) {
        try {
            ProfileDTO entity = new ProfileDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setRoles(getRoles());
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<ProfileDTO> mockProfileDTOList() {
        List<ProfileDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockProfileDTO(i));
        }
        return entitys;
    }

    public Set<RoleEnum> getRoles() {
        if (CollectionUtils.isEmpty(roles)) {
            roles = new HashSet<>();
            roles.add(RoleEnum.ADMIN);
        }
        return roles;
    }
}
