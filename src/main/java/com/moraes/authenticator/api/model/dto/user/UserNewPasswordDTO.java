package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.NotEmpty;

public record UserNewPasswordDTO(@NotEmpty String oldPassword, @NotEmpty String newPassword) {

}
