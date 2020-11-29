package com.omblanco.springboot.webflux.api.mongo.app.model.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.omblanco.springboot.webflux.api.mongo.app.model.entity.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String>, CustomUserRepository {
    
    /**
     * Busca un usuario por email
     * @param email email
     * @return Usuario
     */
    Mono<User> findByEmail(String email);
    
    /**
     * No funciona Aggregation en ReactiveMongoRepository
     * @param aggregation
     * @param pageable
     * @return
     */
    Flux<User> findBy(Aggregation aggregation, Pageable pageable);
    Mono<Long> countBy(Aggregation aggregation);
    
}
