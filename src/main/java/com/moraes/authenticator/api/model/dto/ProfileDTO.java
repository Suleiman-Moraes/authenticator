package com.moraes.authenticator.api.model.dto;

import java.io.Serializable;
import java.util.Set;

import com.moraes.authenticator.api.model.enums.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO implements Serializable {

    private String description;

    private Set<RoleEnum> roles;
}
