package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserNewPasswordDTO(@NotBlank String oldPassword, @NotBlank String newPassword) {

}
