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

    /**
     * This code snippet is a method implementation in Java. It generates a paged
     * result based on the provided filter and criteria. It takes in several
     * parameters such as the filter, join criteria, fields to retrieve, and class
     * types. It builds queries using the provided parameters, executes the queries,
     * and returns a paged result of type T. The method uses StringBuilder to
     * construct the queries and log.debug to log the generated queries and the
     * final result. The paged result is returned as a Page<T> object.
     *
     * @param filter       the filter to apply when generating the page
     * @param fields       the field mappings to use when generating the page
     * @param classListDto the class of the list DTO for the page
     * @param classFrom    the class of the object to generate the page from
     * @return the generated page of objects
     */
    default <T> Page<T> page(FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom) {
        return page(filter, "", fields, classListDto, classFrom);
    }

    /**
     * This code snippet is a method implementation in Java. It generates a paged
     * result based on the provided filter and criteria. It takes in several
     * parameters such as the filter, join criteria, fields to retrieve, and class
     * types. It builds queries using the provided parameters, executes the queries,
     * and returns a paged result of type T. The method uses StringBuilder to
     * construct the queries and log.debug to log the generated queries and the
     * final result. The paged result is returned as a Page<T> object.
     *
     * @param filter       the filter to apply to the query
     * @param join         the join criteria for the query
     * @param fields       the fields to retrieve in the query
     * @param classListDto the class type of the DTO for the result list
     * @param classFrom    the class type of the entity to query from
     * @param paramName    description of parameter
     * @return the paged result of type T
     */
    <T> Page<T> page(FilterDTO filter, final String join, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom);
}
