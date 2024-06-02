package com.moraes.authenticator.api.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.moraes.authenticator.api.mapper.PersonMapper;
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
import com.moraes.authenticator.api.model.enums.RoleEnum;
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

    private PersonMapper personMapper;

    @Transactional
    @Override
    public void update(PersonDTO object, Long key) {
        Person entity = findByKeyAndCompanyKey(key);
        final Long userId = entity.getUser().getKey();
        saveForUpdate(Mapper.parseObjectForUpdate(object, entity), entity);
        userService.update(object.getUser(), userId);
    }

    @Transactional
    @Override
    public Long updateMe(PersonDTO object) {
        Person entity = this.getMe();
        final Long userId = entity.getUser().getKey();
        userService.validMe(userId, object.getUser().getUsername());
        personMapper.updateFromPersonDTO(entity, object);
        repository.save(entity);
        userService.updateMe(object.getUser(), entity.getUser());
        return entity.getKey();
    }

    @Transactional
    @Override
    public void delete(Long key) {
        Person entity = findByKeyAndCompanyKey(key);
        userService.delete(entity.getUser().getKey());
        repository.delete(entity);
    }

    @Transactional
    @Override
    public Long insert(Person object) {
        userService.validInsert(object.getUser());
        repository.save(object);
        userService.insertForAdmin(object.getUser(), object.getKey());
        return object.getKey();
    }

    @Transactional
    @Override
    public Long insertMe(Person object) {
        userService.preInsertMe(object.getUser());
        return insert(object);
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
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("companyKey", getMe().getUser().getCompany().getKey());
        parameters.put("root", RoleEnum.ROOT);
        Page<PersonListDTO> page = repository.page(filter, fields, PersonListDTO.class, Person.class,
                "x.user.company.key = :companyKey AND :root NOT IN ELEMENTS(x.user.profile.roles)", parameters);
        page.getContent().forEach(dto -> addLinks(dto, (long) dto.getKey(), PersonController.class));
        return page;
    }

    @Transactional(readOnly = true)
    @Override
    public Person findByKeyAndCompanyKey(Long key) {
        return repository.findByKeyAndUserCompanyKey(key, getMe().getUser().getCompany().getKey())
                .orElseThrow(ResourceNotFoundException::new);
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
        repository.save(entityNew);
        // Set user = null for avoid unsaved transient instance
        entity.setUser(null);
    }
}
