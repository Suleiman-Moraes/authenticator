package com.moraes.authenticator.api.model.dto.person;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.moraes.authenticator.api.model.dto.user.UserDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
public class PersonDTO extends RepresentationModel<PersonDTO> implements Serializable, IPersonDTO {

    @NotBlank
    @Size(min = 2, max = 150)
    private String name;

    @NotBlank
    @Size(min = 5, max = 150)
    @Email
    private String email;

    @Size(max = 255)
    private String address;

    @NotNull
    @Valid
    private UserDTO user;
}
