package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - E findByKey(I key);
 */
public interface IServiceFindByKey<E, I> extends IServiceGetRepository<E, I> {

    default E findByKey(I key) {
        return getRepository().findById(key).orElseThrow(ResourceNotFoundException::new);
    }
}
