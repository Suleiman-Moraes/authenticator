package com.moraes.authenticator.api.util;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.moraes.authenticator.api.model.User;
import com.moraes.authenticator.api.model.dto.PermissionDTO;
import com.moraes.authenticator.api.model.enums.RoleEnum;

public class SecurityUtil {

    private SecurityUtil() {
    }

    public static boolean hasRole(RoleEnum roleEnum) {
        return getAuthorities().contains(PermissionDTO.builder()
                .role(roleEnum)
                .build());
    }

    public static boolean hasRoleAdmin() {
        return hasRole(RoleEnum.ADMIN);
    }

    public static List<RoleEnum> getRoles() {
        return getAuthorities().stream()
                .map(a -> RoleEnum.valueOf(a.getAuthority()))
                .toList();
    }

    public static User getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() != null && authentication.getPrincipal() instanceof User user) {
			return user;
		}
		return null;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities() {
        return Optional.ofNullable(Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .orElse(new UsernamePasswordAuthenticationToken(null, null)).getAuthorities())
                .orElse(new LinkedList<>());
    }
}
