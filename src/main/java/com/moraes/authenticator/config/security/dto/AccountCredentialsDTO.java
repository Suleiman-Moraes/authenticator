package com.moraes.authenticator.config.security.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCredentialsDTO implements Serializable {

    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
