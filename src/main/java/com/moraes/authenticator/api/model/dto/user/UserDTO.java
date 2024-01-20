package com.moraes.authenticator.api.model.dto.user;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.dto.KeyDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO implements Serializable, IUserDTO {

    @NotBlank
    @Size(min = 2, max = 150)
    private String username;
    
    @Size(min = 6, max = 30)
    private String password;

    @NotNull
    @Valid
    private KeyDTO profile;
}
