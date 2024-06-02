package com.moraes.authenticator.api.model.dto.menu.question;

import java.io.Serializable;

import com.moraes.authenticator.api.model.enums.TypeEnum;

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
public class QuestionAllDTO implements Serializable {

    @NotBlank
    @Size(min = 3, max = 255)
    private String value;

    @Size(min = 1, max = 50)
    private String mask;

    @NotNull
    private TypeEnum type;

    @Builder.Default
    private boolean required = false;
}
