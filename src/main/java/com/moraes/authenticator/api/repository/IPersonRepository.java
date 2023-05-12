package com.moraes.authenticator.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moraes.authenticator.api.model.Person;

public interface IPersonRepository extends JpaRepository<Person, Long> {

}
