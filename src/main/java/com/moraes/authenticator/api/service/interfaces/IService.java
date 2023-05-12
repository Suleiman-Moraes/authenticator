package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.interfaces.IModel;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - List<E> findAll();
 * - E findByKey(I id);
 * - I insert(E object);
 * - void delete(I id);
 * - <T extends RepresentationModel<T>> List<T> parseObjects(List<E> listE,
 * Class<T> clazz, Class<? extends IController<?, I>> controllerClass);
 * - <T extends RepresentationModel<T>> T parseObject(E e, Class<T> clazz,
 * Class<? extends IController<?, I>> controllerClass);
 * - <T extends RepresentationModel<T>> T parseObjectAfterValid(E e, Class<T>
 * clazz, Class<? extends IController<?, I>> controllerClass);
 */
public interface IService<E extends IModel<I>, I> extends IServiceCRUD<E, I>, IConverterService<E, I> {

}
