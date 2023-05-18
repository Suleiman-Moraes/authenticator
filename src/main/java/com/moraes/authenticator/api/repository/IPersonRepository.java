package com.moraes.authenticator.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.Person;

public interface IPersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByUserKey(Long userKey);
}
