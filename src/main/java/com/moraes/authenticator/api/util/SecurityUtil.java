package com.moraes.authenticator.api.util;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;

import com.moraes.authenticator.api.model.dto.PermissionDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static boolean hasRole(RoleEnum roleEnum) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(PermissionDTO.builder()
                .role(roleEnum)
                .build());
    }

    public static boolean hasRoleAdmin() {
        return hasRole(RoleEnum.ADMIN);
    }

    public static List<RoleEnum> getRoles() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(a -> RoleEnum.valueOf(a.getAuthority()))
                .toList();
    }
}
