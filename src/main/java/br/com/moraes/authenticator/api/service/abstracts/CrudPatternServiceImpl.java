package br.com.moraes.authenticator.api.service.abstracts;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.moraes.authenticator.api.exception.IdNotInformedException;
import br.com.moraes.authenticator.api.exception.ObjectNotFoundException;
import br.com.moraes.authenticator.api.interfaces.ICrudPatternService;
import br.com.moraes.authenticator.api.model.Model;

public abstract class CrudPatternServiceImpl<T> implements ICrudPatternService<T> {

    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    @Override
    public Optional<T> findById(Long id) {
        return getRepository().findById(id);
    }

    @Override
    public T save(T objeto) {
        return getRepository().save(objeto);
    }

    @Override
    public T create(T objeto) {
        if (objeto instanceof Model) {
            ((Model) objeto).setId(null);
        }
        return save(objeto);
    }

    @Override
    public T update(T objeto, Long id) throws Exception {
        if (!validId(id)) {
            throw new ObjectNotFoundException();
        }
        objeto.getClass().getMethod("setId", Long.class).invoke(objeto, id);
        return save(objeto);
    }

    @Override
    public void delete(Long id) {
        if (!validId(id)) {
            throw new ObjectNotFoundException();
        }
        getRepository().deleteById(id);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10, Sort.by(Direction.DESC, "id"));
        }
        return getRepository().findAll(pageable);
    }

    @Override
    public boolean validId(Long id) {
        if (id == null || id <= 0) {
            throw new IdNotInformedException();
        }
        return getRepository().existsById(id);
    }

    public abstract JpaRepository<T, Long> getRepository();
}