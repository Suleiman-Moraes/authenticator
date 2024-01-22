package com.moraes.authenticator.api.model.dto.user;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserResetPasswordTokenDTO(

        @NotBlank @Size(min = 6, max = 30) String password,

        @NotNull UUID token) {

}
