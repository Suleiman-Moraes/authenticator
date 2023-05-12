package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.exception.ResourceNotFoundException;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - E findByKey(I id);
 */
public interface IServiceFindByKey<E, I> extends IServiceGetRepository<E, I> {

    default E findByKey(I id) {
        return getRepository().findById(id).orElseThrow(ResourceNotFoundException::new);
    }
}
