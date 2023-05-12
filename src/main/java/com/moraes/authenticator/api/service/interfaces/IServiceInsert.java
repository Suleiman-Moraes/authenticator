package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.interfaces.IModel;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - I insert(E object);
 */
public interface IServiceInsert<E extends IModel<I>, I> extends IServiceGetRepository<E, I> {

    default I insert(E object) {
        getRepository().save(object);
        return object.getKey();
    }
}
