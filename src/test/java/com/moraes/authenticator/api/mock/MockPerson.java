package com.moraes.authenticator.api.mock;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.mock.interfaces.AbstractMock;
import com.moraes.authenticator.api.model.Person;
import com.moraes.authenticator.api.model.dto.person.PersonDTO;
import com.moraes.authenticator.api.model.dto.person.PersonListDTO;
import com.moraes.authenticator.api.model.dto.person.PersonMeDTO;
import com.moraes.authenticator.api.util.MockUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class MockPerson extends AbstractMock<Person> {

    private int maxSize = 14;

    @Override
    protected Class<Person> getClazz() {
        return Person.class;
    }

    @Override
    protected void setOdersValues(Person entity, Integer number) {
        entity.setUser(new MockUser().mockEntity(number));
    }

    public PersonDTO mockPersonDTO(Integer number) {
        try {
            PersonDTO entity = new PersonDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setUser(new MockUser().mockUserDTO(number));
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<PersonDTO> mockPersonDTOList() {
        List<PersonDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= 14; i++) {
            entitys.add(mockPersonDTO(i));
        }
        return entitys;
    }

    public PersonMeDTO mockPersonMeDTO(int number) {
        try {
            PersonMeDTO entity = new PersonMeDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            entity.setUser(new MockUser().mockUserMeDTO(number));
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<PersonListDTO> mockPersonListDTOList(int maxSize) {
        List<PersonListDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= maxSize; i++) {
            entitys.add(mockPersonListDTO(i));
        }
        return entitys;
    }

    public List<PersonListDTO> mockPersonListDTOListWithKey(int maxSize) {
        List<PersonListDTO> entitys = new LinkedList<>();
        for (int i = 1; i <= maxSize; i++) {
            PersonListDTO entity = mockPersonListDTO(i);
            entity.setKey(Integer.valueOf(i).longValue());
            entitys.add(entity);
        }
        return entitys;
    }

    private PersonListDTO mockPersonListDTO(int number) {
        try {
            PersonListDTO entity = new PersonListDTO();
            MockUtil.toFill(entity, number, ignoreFields);
            // setOdersValues
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }
}
