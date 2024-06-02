package com.moraes.authenticator.api.model.dto.menu.question;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;

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
public class QuestionListDTO extends RepresentationModel<QuestionListDTO> implements Serializable {

    private Long key;

    private String value;

    private String mask;

    private Integer order;

    private TypeFromEnum typeFrom;

    private TypeEnum type;

    private boolean enabled;

    private boolean required;
}
