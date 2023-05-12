package com.moraes.authenticator.api.controller.interfaces;

import org.springframework.http.ResponseEntity;

public interface IController<D, I> {

    ResponseEntity<D> findByKey(I id);
}
