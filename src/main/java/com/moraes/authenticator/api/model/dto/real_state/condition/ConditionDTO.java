package com.moraes.authenticator.api.model.dto.real_state.condition;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.enums.FrequencyEnum;
import com.moraes.authenticator.api.validation.interfaces.IDayValidBetween5O10O15;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConditionDTO implements Serializable {

    @NotNull
    private FrequencyEnum frequency;

    @NotNull
    @Positive
    private Integer numberInstallments;

    @Positive
    @NotNull
    private BigDecimal valueInstallments;

    @NotNull
    @FutureOrPresent
    @IDayValidBetween5O10O15
    private LocalDate beginningInstallment;
}
