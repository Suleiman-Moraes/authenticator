package com.moraes.authenticator.api.service.interfaces;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.util.CollectionUtils;

import com.moraes.authenticator.api.controller.interfaces.IController;
import com.moraes.authenticator.api.exception.ResourceNotFoundException;
import com.moraes.authenticator.api.mapper.Mapper;
import com.moraes.authenticator.api.model.interfaces.IModel;

/**
 * E = Entity
 * I = ID
 * Methods:
 * - <T extends RepresentationModel<T>> List<T> parseObjects(List<E> listE,
 * Class<T> clazz, Class<? extends IController<?, I>> controllerClass);
 * - <T extends RepresentationModel<T>> T parseObject(E e, Class<T> clazz,
 * Class<? extends IController<?, I>> controllerClass);
 * - <T extends RepresentationModel<T>> T parseObjectAfterValid(E e, Class<T>
 * clazz, Class<? extends IController<?, I>> controllerClass);
 */
public interface IConverterService<E extends IModel<I>, I> {

    default <T extends RepresentationModel<T>> List<T> parseObjects(List<E> listE, Class<T> clazz,
            Class<? extends IController<?, I>> controllerClass) {
        if (CollectionUtils.isEmpty(listE)) {
            throw new ResourceNotFoundException();
        }
        return listE.stream().map((E e) -> parseObjectAfterValid(e, clazz, controllerClass))
                .collect(Collectors.toList());
    }

    default <T extends RepresentationModel<T>> T parseObject(E e, Class<T> clazz,
            Class<? extends IController<?, I>> controllerClass) {
        if (e == null || e.getKey() == null) {
            throw new ResourceNotFoundException();
        }
        return parseObjectAfterValid(e, clazz, controllerClass);
    }

    default <T extends RepresentationModel<T>> T parseObjectAfterValid(E e, Class<T> clazz,
            Class<? extends IController<?, I>> controllerClass) {
        T t = Mapper.parseObject(e, clazz);
        addLinks(t, e.getKey(), controllerClass);
        return t;
    }

    default <T extends RepresentationModel<T>> void addLinks(T t, I key,
            Class<? extends IController<?, I>> controllerClass) {
        t.add(linkTo(methodOn(controllerClass).findByKey(key)).withSelfRel());
    }
}
