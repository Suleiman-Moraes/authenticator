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
    public <T> Page<T> page(FilterDTO filter, final String join, final Map<String, Class<?>> fields,
            final Class<T> classListDto, final Class<M> classFrom) {
        StringBuilder where = new StringBuilder();
        StringBuilder query = new StringBuilder();
        StringBuilder fromHql = new StringBuilder();
        buildQueries(filter, join, fields, classListDto, classFrom, where, query, fromHql);
        final String countQuery = "SELECT COUNT(x.id".concat(fromHql.toString());
        log.debug("HQL count query: {}", countQuery);
        log.debug("HQL query: {}", query);
        final Long count = setParameterSearchTextInTypedQuery(entityManager.createQuery(countQuery, Long.class),
                filter.getSearchText()).getSingleResult();

        TypedQuery<T> typedQueryList = setParameterSearchTextInTypedQuery(
                entityManager.createQuery(query.toString(), classListDto), filter.getSearchText());
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
     * This code snippet is a method called buildQueries that takes in several
     * parameters and constructs a query string based on those parameters. The
     * method builds a WHERE clause for the query based on the filter parameter and
     * the fields map. It also constructs the SELECT and FROM parts of the query
     * based on the classListDto and classFrom parameters. Finally, it appends an
     * ORDER BY clause to the query based on the property and filter parameters.
     *
     * @param filter       the filter object containing the filter criteria
     * @param join         the join criteria for the query
     * @param fields       the map of fields and their corresponding classes
     * @param classListDto the class of the DTO to be returned in the query result
     * @param classFrom    the class from which data is being fetched
     * @param where        the StringBuilder object for building the WHERE clause
     * @param query        the StringBuilder object for building the SELECT clause
     * @param fromHql      the StringBuilder object for building the FROM clause
     */
    protected <T> void buildQueries(FilterDTO filter, String join, final Map<String, Class<?>> fields,
            Class<T> classListDto, Class<M> classFrom, StringBuilder where, StringBuilder query,
            StringBuilder fromHql) {
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
    protected <T> TypedQuery<T> setParameterSearchTextInTypedQuery(TypedQuery<T> typedQuery, String searchText) {
        if (StringUtils.hasText(searchText)) {
            typedQuery.setParameter("searchText", "%" + searchText.toUpperCase() + "%");
        }
        return typedQuery;
    }

}
