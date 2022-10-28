package br.com.moraes.authenticator.api.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICrudPatternService<T> {

    List<T> findAll();

    Optional<T> findById(Long id);

    T save(T objeto);

    T create(T objeto);

    T update(T objeto, Long id) throws Exception;

    void delete(Long id);

    boolean validId(Long id);

    Page<T> findAll(Pageable pageable);
}