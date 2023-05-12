package com.moraes.authenticator.api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.PersonDTO;
import com.moraes.authenticator.api.repository.IPersonRepository;
import com.moraes.authenticator.api.service.interfaces.IPersonService;
import com.moraes.authenticator.api.service.interfaces.IUserService;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class PersonService implements IPersonService {

    @Getter
    private IPersonRepository repository;

    private IUserService userService;

    @Transactional
    @Override
    public void update(PersonDTO object, Long key) {
        Person entity = findByKey(key);
        Person entityNew = Mapper.parseObject(object, Person.class);
        entityNew.setKey(entity.getKey());
        repository.save(entityNew);

        entityNew.getUser().setKey(entity.getUser().getKey());
        userService.update(entityNew.getUser(), key);
    }

    @Transactional
    @Override
    public void delete(Long key) {
        Person entity = findByKey(key);
        userService.delete(entity.getUser().getKey());
        repository.delete(entity);
    }
}
