package com.moraes.authenticator.api.mock;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.Profile;
import com.moraes.authenticator.api.model.dto.ProfileDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockProfile extends AbstractMock<Profile> {

    private Set<RoleEnum> roles = Set.of(RoleEnum.ROOT);

    @Override
    protected Class<Profile> getClazz() {
        return Profile.class;
    }

    @Override
    protected void setOdersValues(Profile entity, Integer number) {
        entity.setRoles(roles);
    }

    public ProfileDTO mockProfileDTO(Integer number) {
        try {
            ProfileDTO entity = new ProfileDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setRoles(roles);
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
}
