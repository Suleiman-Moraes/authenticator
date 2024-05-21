package com.moraes.authenticator.api.mock.interfaces;

import java.util.LinkedList;
import java.util.List;

import com.moraes.authenticator.api.util.MockUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractMock<E> {

    protected List<String> ignoreFields = null;

    protected AbstractMock() {
        ignoreFields = new LinkedList<>();
        ignoreFields.add("key");
    }

    public E mockEntity(Integer number) {
        try {
            E entity = getClazz().getDeclaredConstructor().newInstance();
            MockUtil.toFill(entity, number, ignoreFields);
            setOdersValues(entity, number);
            return entity;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        return null;
    }

    public List<E> mockEntityList() {
        return mockEntityList(14);
    }

    public List<E> mockEntityList(int size) {
        List<E> entitys = new LinkedList<>();
        for (int i = 1; i <= size; i++) {
            entitys.add(mockEntity(i));
        }
        return entitys;
    }

    protected abstract void setOdersValues(E entity, Integer number);

    protected abstract Class<E> getClazz();
}