package com.moraes.authenticator.api.model.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserEnabledDTO(@NotNull Boolean enabled) {

}
