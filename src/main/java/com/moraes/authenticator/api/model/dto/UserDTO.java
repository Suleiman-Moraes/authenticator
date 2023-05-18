package com.moraes.authenticator.api.model.dto;

import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable {

    @NotBlank
    private String username;
    
    private String password;

    @NotNull
    @Valid
    private KeyDTO profile;
}
