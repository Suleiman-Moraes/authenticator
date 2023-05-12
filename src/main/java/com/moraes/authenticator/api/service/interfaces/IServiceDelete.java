package com.moraes.authenticator.api.service.interfaces;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - void delete(I id);
 */
public interface IServiceDelete<E, I> extends IServiceFindByKey<E, I> {

    default void delete(I key) {
        getRepository().delete(findByKey(key));
    }
}
