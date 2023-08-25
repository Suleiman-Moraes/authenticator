package com.moraes.authenticator.api.service;

import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.controller.PersonController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
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
        final Long userId = entity.getUser().getKey();
        Person entityNew = parseObjectForUpdate(object);
        // validate here
        entityNew.setKey(entity.getKey());
        repository.save(entityNew);

        userService.update(object.getUser(), userId);
    }

    @Transactional
    @Override
    public void delete(Long key) {
        Person entity = findByKey(key);
        userService.delete(entity.getUser().getKey());
        repository.delete(entity);
    }

    @Transactional
    @Override
    public Long insert(Person object) {
        userService.validInsert(object.getUser());
        repository.save(object);
        userService.insert(object.getUser(), object.getKey());
        return object.getKey();
    }

    @Transactional(readOnly = true)
    @Override
    public Person getMe() {
        return repository.findByUserKey(userService.getMe().getKey()).orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional
    @Override
    public Long updateMe(PersonDTO object) {
        final Long key = getMe().getKey();
        update(object, key);
        return key;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonListDTO> findPageAll(PersonFilterDTO filter) {
        if (filter.isPaginate()) {
            final Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), filter.getDirection(),
                    filter.getProperty());
            return new PageImpl<>(new LinkedList<>(), pageable, 0);
        }
        final Sort sort = Sort.by(filter.getDirection(), filter.getProperty());
        final List<Person> list = repository.findAll(sort);
        final Pageable pageable = PageRequest.of(0, list.size(), sort);
        return new PageImpl<>(parseObjects(list, PersonListDTO.class, PersonController.class), pageable, list.size());
    }

    public Person parseObjectForUpdate(PersonDTO object) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<PersonDTO, Person>() {
            @Override
            protected void configure() {
                skip(destination.getUser());
            }
        });
        return modelMapper.map(object, Person.class);
    }
}
