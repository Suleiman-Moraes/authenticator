package com.moraes.authenticator.api.model.dto.menu.question;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.enums.TypeEnum;
import com.moraes.authenticator.api.model.enums.TypeFromEnum;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class QuestionDTO extends RepresentationModel<QuestionDTO> implements Serializable {

    @NotBlank
    @Size(min = 3, max = 255)
    private String value;

    @Size(min = 1, max = 50)
    private String mask;

    @Min(1)
    private Integer order;

    @NotNull
    private TypeFromEnum typeFrom;

    @NotNull
    private TypeEnum type;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean required = false;
}
