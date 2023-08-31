package com.moraes.authenticator.api.service.interfaces;

import org.springframework.data.domain.Page;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;

public interface IPersonService extends IService<Person, Long> {

    void update(PersonDTO object, Long id);

    Person getMe();

    Long updateMe(PersonMeDTO object);

    Page<PersonListDTO> findPageAll(PersonFilterDTO filter);

    Long insertMe(Person object);
}
