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

    protected <T> TypedQuery<T> setParameterSearchTextInTypedQuery(TypedQuery<T> typedQuery, String searchText) {
        if (StringUtils.hasText(searchText)) {
            typedQuery.setParameter("searchText", "%" + searchText.toUpperCase() + "%");
        }
        return typedQuery;
    }

}
