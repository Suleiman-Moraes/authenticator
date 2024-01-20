package com.moraes.authenticator.api.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.controller.PersonController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
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
        saveForUpdate(Mapper.parseObject(object, Person.class), entity);
        userService.update(object.getUser(), userId);
    }

    @Transactional
    @Override
    public Long updateMe(PersonMeDTO object) {
        Person entity = this.getMe();
        final Long userId = entity.getUser().getKey();
        saveForUpdate(Mapper.parseObject(object, Person.class), entity);
        userService.updateMe(object.getUser(), userId);
        return entity.getKey();
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

    @Transactional
    @Override
    public Long insertMe(Person object) {
        userService.preInsertMe(object.getUser());
        return this.insert(object);
    }

    @Transactional(readOnly = true)
    @Override
    public Person getMe() {
        return repository.findByUserKey(userService.getMe().getKey()).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PersonListDTO> findPageAll(PersonFilterDTO filter) {
        final Map<String, Class<?>> fields = getMapOfFields();
        Page<PersonListDTO> page = repository.page(filter, fields, PersonListDTO.class, Person.class);
        page.getContent().forEach(dto -> addLinks(dto, (long) dto.getKey(), PersonController.class));
        return page;
    }

    public Map<String, Class<?>> getMapOfFields() {
        final Map<String, Class<?>> fields = new LinkedHashMap<>();
        fields.put("x.key", Number.class);
        fields.put("x.name", String.class);
        fields.put("x.address", String.class);
        fields.put("x.email", String.class);
        fields.put("x.user.username", String.class);
        fields.put("x.user.profile.description", String.class);
        return fields;
    }

    /**
     * 
     * @param entityNew
     * @param entity
     */
    private void saveForUpdate(Person entityNew, Person entity) {
        // validate here
        entityNew.setKey(entity.getKey());
        repository.save(entityNew);
        // Set user = null for avoid unsaved transient instance
        entity.setUser(null);
    }
}
