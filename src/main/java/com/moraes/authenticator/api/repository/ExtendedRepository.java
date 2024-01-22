package com.moraes.authenticator.api.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.moraes.authenticator.api.model.dto.FilterDTO;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtendedRepository<M, K extends Serializable> extends SimpleJpaRepository<M, K>
        implements IExtendedRepository<M, K> {

    private EntityManager entityManager;

    public ExtendedRepository(JpaEntityInformation<M, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public <T> Page<T> page(FilterDTO filter, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom, final String whereCustom,
            Map<String, Object> parameters, final String join) {
        StringBuilder where = new StringBuilder();
        StringBuilder query = new StringBuilder();
        StringBuilder fromHql = new StringBuilder();
        buildQueries(filter, join, fields, classListDto, classFrom, where, query, fromHql, whereCustom);
        final String countQuery = "SELECT COUNT(x.id".concat(fromHql.toString());
        log.debug("HQL count query: {}", countQuery);
        log.debug("HQL query: {}", query);
        final Long count = setParameterSearchTextInTypedQuery(entityManager.createQuery(countQuery, Long.class),
                filter.getSearchText(), parameters).getSingleResult();

        TypedQuery<T> typedQueryList = setParameterSearchTextInTypedQuery(
                entityManager.createQuery(query.toString(), classListDto), filter.getSearchText(), parameters);
        treatTypedQueryAccordingToPassedType(filter, count, typedQueryList);
        final List<T> list = typedQueryList.getResultList();

        final Page<T> page = new PageImpl<>(list != null ? list : new LinkedList<>(),
                PageRequest.of(filter.getPage(), filter.getSize(),
                        Sort.by(filter.getDirection(), filter.getProperty())),
                count);

        log.debug("Return: {}", page.getContent());

        return page;
    }

    /**
     * This code snippet defines a method called
     * treatTypedQueryAccordingToPassedType that takes in three parameters: filter,
     * count, and typedQueryList.
     * 
     * The method checks if the filter object has pagination enabled. If pagination
     * is enabled, it calculates the offset and sets the maximum number of results
     * for the typedQueryList based on the filter parameters. If pagination is not
     * enabled, it sets the page to 0 and the size to the total count.
     * 
     * The method is generic, meaning it can work with any type T for the
     * typedQueryList.
     *
     * @param filter         the filter object containing pagination and size
     *                       parameters
     * @param count          the total count of items
     * @param typedQueryList the typed query object to be treated
     */
    protected <T> void treatTypedQueryAccordingToPassedType(FilterDTO filter, final Long count,
            TypedQuery<T> typedQueryList) {
        if (filter.isPaginate()) {
            final int lastPosition = count.intValue() - 1;
            int lastPage = lastPosition / filter.getSize();
            int offset = filter.getPage() * filter.getSize();
            if (offset >= count) {
                filter.setPage(lastPage);
                offset = filter.getSize() * lastPage;
            }
            typedQueryList.setMaxResults(filter.getSize()).setFirstResult(offset);
        } else {
            filter.setPage(0);
            filter.setSize(count.intValue());
        }
    }

    /**
     * This Java code defines a method for building queries based on given filter
     * conditions and fields. It takes in various parameters such as filter, join
     * condition, fields, and classes, and constructs a SQL query based on these
     * inputs. The method also handles different data types for the fields and
     * allows for custom WHERE clauses. Additionally, it constructs the SELECT and
     * FROM clauses for the query and applies ordering based on the filter
     * conditions.
     *
     * @param filter       the filter to be applied to the queries
     * @param join         the join condition for the query
     * @param fields       the fields to be queried
     * @param classListDto the class for the list DTO
     * @param classFrom    the class from which the query is built
     * @param where        the WHERE clause for the query
     * @param query        the SELECT clause for the query
     * @param fromHql      the FROM clause for the query
     * @param whereCustom  custom WHERE clause for the query
     */
    protected <T> void buildQueries(FilterDTO filter, String join, final Map<String, Class<?>> fields,
            Class<T> classListDto, Class<M> classFrom, StringBuilder where, StringBuilder query,
            StringBuilder fromHql, final String whereCustom) {
        where.append("( 1 != 1");
        query.append("SELECT new ");
        query.append(classListDto.getName()).append("(");
        String property = "x.key";
        filter.setProperty("x.".concat(filter.getProperty()));
        for (Map.Entry<String, Class<?>> entry : fields.entrySet()) {
            final String key = entry.getKey();
            final Class<?> value = entry.getValue();
            query.append(key).append(", ");

            if (key.equalsIgnoreCase(filter.getProperty())) {
                property = key;
            }
            if (value.equals(Number.class)) {
                where.append(" OR CAST(").append(key);
                where.append(" AS string) LIKE REPLACE(:searchText, ',', '.')");
            } else if (value.equals(LocalDateTime.class)) {
                where.append(" OR FORMAT(").append(key).append(", 'dd/MM/yyyy HH:mm:ss') LIKE :searchText");
            } else if (value.equals(LocalDate.class)) {
                where.append(" OR FORMAT(").append(key).append(", 'dd/MM/yyyy') LIKE :searchText");
            } else {
                where.append(" OR UPPER(").append(key).append(") LIKE :searchText");
            }
        }
        where.append(")");
        final int length = query.length();
        query = query.delete(length - 2, length);
        fromHql.append(") FROM ");
        fromHql.append(classFrom.getSimpleName()).append(" x ").append(join);
        if (StringUtils.hasText(filter.getSearchText())) {
            fromHql.append(" WHERE ").append(where);
            if (StringUtils.hasText(whereCustom)) {
                fromHql.append(" AND ").append(whereCustom);
            }
        } else if (StringUtils.hasText(whereCustom)) {
            fromHql.append(" WHERE ").append(whereCustom);
        }
        query.append(fromHql);
        query.append(String.format(" ORDER BY %s %s", property, filter.getDirection()));
    }

    /**
     * This code snippet defines a method that takes a TypedQuery and a searchText
     * as input. If the searchText is not empty, it sets a parameter in the
     * typedQuery with the name "searchText" and a value that is the searchText
     * surrounded by "%" symbols and in uppercase. Finally, it returns the modified
     * typedQuery.
     * 
     * @param <T>
     * @param typedQuery
     * @param searchText
     * @return
     */
    protected <T> TypedQuery<T> setParameterSearchTextInTypedQuery(TypedQuery<T> typedQuery, String searchText,
            Map<String, Object> parameters) {
        if (StringUtils.hasText(searchText)) {
            typedQuery.setParameter("searchText", "%" + searchText.toUpperCase() + "%");
        }
        if (!CollectionUtils.isEmpty(parameters)) {
            parameters.forEach(typedQuery::setParameter);
        }
        return typedQuery;
    }

}
