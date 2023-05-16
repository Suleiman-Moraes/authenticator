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
public class PersonDTO implements Serializable {

    @NotBlank
    private String name;

    private String address;

    @NotNull
    @Valid
    private UserDTO user;
}
