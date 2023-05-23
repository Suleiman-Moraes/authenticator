package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.PersonDTO;

public interface IPersonService extends IService<Person, Long> {

    void update(PersonDTO object, Long id);

    Person getMe();

    Long updateMe(PersonDTO object);
}
