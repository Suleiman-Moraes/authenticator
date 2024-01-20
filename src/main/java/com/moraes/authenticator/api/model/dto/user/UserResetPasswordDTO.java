package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.NotBlank;

public record UserResetPasswordDTO(@NotBlank String username, @NotBlank String login) {

}
