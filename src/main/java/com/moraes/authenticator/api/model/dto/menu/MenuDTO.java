package com.moraes.authenticator.api.model.dto.menu;

import java.io.Serializable;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDTO implements Serializable {

    @NotBlank
    @Size(min = 1, max = 50)
    private String label;

    @NotNull
    private List<MenuItemDTO> items;
}
