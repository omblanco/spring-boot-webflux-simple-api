package com.omblanco.springboot.webflux.api.app.model.specifications;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.util.StringUtils;

public abstract class BaseSpecifications {

    protected static void addLikeIgnoreCaseIfNotEmpty(final String fieldValue, final List<Predicate> predicates, final CriteriaBuilder builder, Expression<String> expression) {
        if (StringUtils.hasLength(fieldValue)) {
            predicates.add(builder.like(builder.lower(expression), "%" + fieldValue.toLowerCase() + "%"));
        }
    }
}
