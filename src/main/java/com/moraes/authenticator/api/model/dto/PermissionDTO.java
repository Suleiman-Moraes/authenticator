package com.moraes.authenticator.api.model.dto;

import org.springframework.security.core.GrantedAuthority;

import com.moraes.authenticator.api.model.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionDTO implements GrantedAuthority {

    private RoleEnum role;

    @Override
    public String getAuthority() {
        return role != null ? role.toString() : null;
    }
}
