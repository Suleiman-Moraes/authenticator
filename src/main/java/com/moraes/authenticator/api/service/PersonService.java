package com.moraes.authenticator.api.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moraes.authenticator.api.controller.PersonController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.FilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonFilterDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
import com.moraes.authenticator.api.repository.IPersonRepository;
import com.moraes.authenticator.api.service.interfaces.IPersonService;
import com.moraes.authenticator.api.service.interfaces.IUserService;

import jakarta.persistence.EntityManager;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Service
public class PersonService implements IPersonService {

    @Getter
    private IPersonRepository repository;

    private IUserService userService;

    private EntityManager entityManager;

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
        List<Object[]> objects = entityManager
                .createQuery(
                        "SELECT CAST(p.user.createdDate AS string), FORMAT(p.user.createdDate, 'dd/MM/yyyy'), p.user.createdDate FROM Person p")
                .getResultList();
        objects.forEach(l -> {
            for (Object o : l) {
                System.out.println(o);
            }
        });
        final String join = "";
        final Map<String, Class<?>> fields = new LinkedHashMap<>();
        fields.put("x.key", Number.class);
        fields.put("x.name", String.class);
        fields.put("x.address", String.class);
        fields.put("x.user.username", String.class);
        fields.put("x.user.profile.description", String.class);
        page(filter, join, fields, PersonListDTO.class, Person.class);
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

    public void page(FilterDTO filter, String join, final Map<String, Class<?>> fields, Class<?> classListDto,
            Class<?> classFrom) {
        new PersonListDTO(null, null, null, null, null);
        StringBuilder where = new StringBuilder(" ( 1 != 1");
        StringBuilder query = new StringBuilder("SELECT new ");
        query.append(classListDto.getName()).append("(");
        for (Map.Entry<String, Class<?>> entry : fields.entrySet()) {
            final String key = entry.getKey();
            final Class<?> value = entry.getValue();
            query.append(key).append(", ");
            if (value.equals(Number.class)) {
                where.append(" OR CAST(").append(key);
                where.append(" AS string) LIKE REPLACE(:searchText, ',', '.')");
            } else {
                where.append(" OR UPPER(").append(key).append(") LIKE :searchText");
            }
        }
        where.append(")");
        final int length = query.length();
        query = query.delete(length - 2, length);
        query.append(") FROM ").append(classFrom.getSimpleName()).append(" x ").append(join);
        query.append(" WHERE ").append(where);
        System.out.println(query);
        final List<?> list = entityManager.createQuery(query.toString(), classListDto)
                .setParameter("searchText", "%" + filter.getSearchText().toUpperCase() + "%").getResultList();
        System.out.println("--------------------------------------------------------------------");
        list.forEach(System.out::println);
        System.out.println("--------------------------------------------------------------------");
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
