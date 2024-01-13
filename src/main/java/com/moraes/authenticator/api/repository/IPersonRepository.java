package com.moraes.authenticator.api.repository;

import java.util.Optional;

import com.moraes.authenticator.api.model.Person;

public interface IPersonRepository extends IExtendedRepository<Person, Long> {

    Optional<Person> findByUserKey(Long userKey);
}
