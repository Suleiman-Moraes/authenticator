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
        return page(filter, fields, classListDto, classFrom, "");
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
     * @param fields       the fields to retrieve in the query
     * @param classListDto the class type of the DTO for the result list
     * @param classFrom    the class type of the entity to query from
     * @param paramName    description of parameter
     * @param join         the join criteria for the query
     * @return the paged result of type T
     */
    default <T> Page<T> page(FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom, final String join) {
        return page(filter, fields, classListDto, classFrom, null, null, join);
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
     * @param fields       the fields to retrieve in the query
     * @param classListDto the class type of the DTO for the result list
     * @param classFrom    the class type of the entity to query from
     * @param whereCustom  custom WHERE clause for the query
     * @param parameters   additional parameters for the query
     * @return the paged result of type T
     */
    default <T> Page<T> page(FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom, final String whereCustom,
            Map<String, Object> parameters) {
        return page(filter, fields, classListDto, classFrom, whereCustom, parameters, "");
    }

    /**
     * This code snippet is a method that takes in filter criteria and returns a
     * paginated list of results based on the given filters. It constructs a query
     * using the filter criteria, executes the query, and returns the paginated
     * results as a Page object. The method also logs the HQL count query, HQL
     * query, and the returned content.
     *
     * @param filter       the filter to apply to the page
     * @param fields       the fields to include in the query result
     * @param classListDto the class of the DTO for the query result
     * @param classFrom    the class from which to retrieve the data
     * @param whereCustom  custom WHERE clause for the query
     * @param parameters   additional parameters for the query
     * @param join         the join condition for the query
     * @return the page of results
     */
    <T> Page<T> page(final FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom, final String whereCustom,
            final Map<String, Object> parameters, final String join);
}
