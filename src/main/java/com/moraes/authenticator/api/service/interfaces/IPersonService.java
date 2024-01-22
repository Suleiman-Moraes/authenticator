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

    /**
     * This Java code snippet uses the @Transactional annotation to specify that the
     * method should be executed within a read-only transaction. It then defines a
     * method to find a Person by a specific key and company key, utilizing a
     * repository method to query the database and returning the result or throwing
     * a ResourceNotFoundException if not found.
     *
     * @param key the key to search for
     * @return the person found by the key and company key
     */
    Person findByKeyAndCompanyKey(Long key);

    Long updateMe(PersonMeDTO object);

    Page<PersonListDTO> findPageAll(PersonFilterDTO filter);

    Long insertMe(Person object);
}
