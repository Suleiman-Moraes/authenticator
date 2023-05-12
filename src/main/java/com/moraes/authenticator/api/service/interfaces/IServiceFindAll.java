package com.moraes.authenticator.api.service.interfaces;

import java.util.List;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - List<E> findAll();
 */
public interface IServiceFindAll<E, I> extends IServiceGetRepository<E, I> {

    default List<E> findAll() {
        return getRepository().findAll();
    }
}
