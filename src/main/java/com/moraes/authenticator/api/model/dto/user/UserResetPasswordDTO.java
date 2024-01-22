package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserResetPasswordDTO(

        @NotBlank @Size(min = 2, max = 150) String username,

        @NotBlank @Size(min = 2, max = 150) @Email String email) {

}
