package com.moraes.authenticator.api.model.dto.real_state.proposal;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
public class ProposalListDTO extends RepresentationModel<ProposalListDTO> implements Serializable {

    private Long key;

    private String enterpriseName;

    private String enterpriseUnit;

    private BigDecimal sizeM2;

    private BigDecimal value;

    private LocalDateTime date;
}
