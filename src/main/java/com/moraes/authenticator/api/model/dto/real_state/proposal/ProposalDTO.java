package com.moraes.authenticator.api.model.dto.real_state.proposal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.dto.menu.question.QuestionDTO;
import com.moraes.authenticator.api.model.dto.real_state.condition.ConditionDTO;
import com.moraes.authenticator.api.model.dto.real_state.enterprise.EnterpriseDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProposalDTO extends RepresentationModel<QuestionDTO> implements Serializable {

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

    @Valid
    @NotNull
    private EnterpriseDTO enterprise;

    @NotNull
    @Size(min = 1)
    private List<@NotNull @Valid ConditionDTO> conditions;
}
