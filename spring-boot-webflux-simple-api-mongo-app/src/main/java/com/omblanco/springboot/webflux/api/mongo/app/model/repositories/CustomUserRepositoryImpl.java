package com.omblanco.springboot.webflux.api.mongo.app.model.repositories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Query;

import com.omblanco.springboot.webflux.api.commons.web.dto.UserFilterDTO;
import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementación del repositorio extendido para recuperar los usuarios paginados y ordenados
 * @author oscar.martinezblanco
 *
 */
public class CustomUserRepositoryImpl extends AbstractCustomUserRepositoryImpl implements CustomUserRepository {
    
    private static final String NAME_PARAM = "name";
    private static final String EMAIL_PARAM = "email";
    private static final String SURNAME_PARAM = "surname";
    
    private ReactiveMongoTemplate template;
    
    public CustomUserRepositoryImpl(ReactiveMongoTemplate template) {
        this.template = template;
    }

    @Override
    public Flux<User> findBy(UserFilterDTO filter, Pageable pageable) {
        List<AggregationOperation> operations = new ArrayList<AggregationOperation>();
        
        addIlikeOperation(operations, NAME_PARAM, filter.getName());
        addIlikeOperation(operations, EMAIL_PARAM, filter.getEmail());
        addIlikeOperation(operations, SURNAME_PARAM, filter.getSurname());
        addSortOperation(operations, pageable.getSort());
        addPageableOperations(operations, pageable);

        Aggregation aggregate = Aggregation.newAggregation(operations);
        
        return template.aggregate(aggregate, User.class, User.class);
    }
    
    @Override
    public Mono<Long> countBy(UserFilterDTO filter) {
        
        Query query = new Query();
        
        addILikeCriteriaToQuery(query, NAME_PARAM, filter.getName());
        addILikeCriteriaToQuery(query, EMAIL_PARAM, filter.getEmail());
        addILikeCriteriaToQuery(query, SURNAME_PARAM, filter.getSurname());
        
        return template.count(query, User.class);
    }
}
