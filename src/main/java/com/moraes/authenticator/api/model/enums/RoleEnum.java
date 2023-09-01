package com.moraes.authenticator.api.model.enums;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.model.dto.PermissionDTO;
import com.moraes.authenticator.api.model.interfaces.IDescription;
import com.moraes.authenticator.api.util.ConstantsUtil;

import lombok.Getter;

@Getter
public enum RoleEnum implements IDescription {
    ROOT("Root"),
    ADMIN("Admin"),
    COMMON_USER("Common user");

    private String description;

    private RoleEnum(String description) {
        this.description = description;
    }

    public String getRoleUnderline() {
        return new StringBuilder(ConstantsUtil.ROLE_UNDERLINE).append(this).toString();
    }

    public static List<PermissionDTO> generate(Collection<RoleEnum> roles) {
        return CollectionUtils.isEmpty(roles) ? new LinkedList<>() : roles.stream().map(PermissionDTO::new).toList();
    }
}
