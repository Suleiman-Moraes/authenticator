package com.moraes.authenticator.api.repository;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.moraes.authenticator.api.model.dto.FilterDTO;

@NoRepositoryBean
public interface IExtendedRepository<M, K extends Serializable>
                extends JpaRepository<M, K> {

        default<T> Page<T> page(FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom){
                return  page(filter, "", fields, classListDto, classFrom);
            }

        <T> Page<T> page(FilterDTO filter, final String join, final Map<String, Class<?>> fields,
                        final Class<T> classListDto, final Class<M> classFrom);
}
