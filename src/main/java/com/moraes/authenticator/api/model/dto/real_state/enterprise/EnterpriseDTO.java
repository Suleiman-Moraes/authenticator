package com.moraes.authenticator.api.model.dto.real_state.enterprise;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnterpriseDTO implements Serializable {

    @NotNull
    @Positive
    private BigDecimal value;

    @NotNull
    @PositiveOrZero
    private BigDecimal vpl;

    @NotNull
    @Positive
    private BigDecimal valueM2;

    @NotNull
    @Positive
    private BigDecimal sizeM2;

    @Size(max = 100)
    @NotBlank
    private String name;

    @Size(max = 100)
    private String unit;

    @NotBlank
    @Size(max = 100)
    private String constructionName;
}
