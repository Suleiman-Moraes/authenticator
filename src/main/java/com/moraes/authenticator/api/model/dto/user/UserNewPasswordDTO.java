package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserNewPasswordDTO(

        @NotBlank @Size(min = 6, max = 30) String oldPassword,

        @NotBlank @Size(min = 6, max = 30) String newPassword

) {
}
